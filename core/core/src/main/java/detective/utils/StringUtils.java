package detective.utils;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Random;

import org.elasticsearch.common.Base64;

import com.google.common.base.Optional;

public class StringUtils {
  
  private static class SecureRandomHolder {
    // class loading is atomic - this is a lazy & safe singleton
    private static final SecureRandom INSTANCE = new SecureRandom();
}
  
  /**
   * Returns a Base64 encoded version of a Version 4.0 compatible UUID
   * as defined here: http://www.ietf.org/rfc/rfc4122.txt
   */
  public static String randomBase64UUID() {
      return randomBase64UUID(SecureRandomHolder.INSTANCE);
  }
  
  /**
   * Returns a Base64 encoded version of a Version 4.0 compatible UUID
   * randomly initialized by the given {@link Random} instance
   * as defined here: http://www.ietf.org/rfc/rfc4122.txt
   */
  public static String randomBase64UUID(Random random) {
      final byte[] randomBytes = new byte[16];
      random.nextBytes(randomBytes);
      
      /* Set the version to version 4 (see http://www.ietf.org/rfc/rfc4122.txt)
       * The randomly or pseudo-randomly generated version.
       * The version number is in the most significant 4 bits of the time
       * stamp (bits 4 through 7 of the time_hi_and_version field).*/
      randomBytes[6] &= 0x0f;  /* clear the 4 most significant bits for the version  */
      randomBytes[6] |= 0x40;  /* set the version to 0100 / 0x40 */
      
      /* Set the variant: 
       * The high field of th clock sequence multiplexed with the variant.
       * We set only the MSB of the variant*/
      randomBytes[8] &= 0x3f;  /* clear the 2 most significant bits */
      randomBytes[8] |= 0x80;  /* set the variant (MSB is set)*/
      try {
          byte[] encoded = Base64.encodeBytesToBytes(randomBytes, 0, randomBytes.length, Base64.URL_SAFE);
          // we know the bytes are 16, and not a multi of 3, so remove the 2 padding chars that are added
          assert encoded[encoded.length - 1] == '=';
          assert encoded[encoded.length - 2] == '=';
          // we always have padding of two at the end, encode it differently
          return new String(encoded, 0, encoded.length - 2, Base64.PREFERRED_ENCODING);
      } catch (IOException e) {
          throw new RuntimeException("should not be thrown");
      }
  }

  public static Optional<String> getBestMatch(String wordToMatch, Collection<String> candidates) {
    if (candidates == null || candidates.size() == 0)
      return Optional.absent();
    
    String bestMatch = null;
    int bestScore = Integer.MAX_VALUE;
    
    for (String candidate : candidates){
      int distance = editDistance(wordToMatch, candidate);
      if (bestScore > distance){
        bestMatch = candidate;
        bestScore = distance;
      }
    }
    
    if (bestMatch == null)
      return Optional.absent();
    else
      return Optional.of(bestMatch);
  }
  
  public static int editDistance(String word1, String word2) {
    int len1 = word1.length();
    int len2 = word2.length();
   
    // len1+1, len2+1, because finally return dp[len1][len2]
    int[][] dp = new int[len1 + 1][len2 + 1];
   
    for (int i = 0; i <= len1; i++) {
      dp[i][0] = i;
    }
   
    for (int j = 0; j <= len2; j++) {
      dp[0][j] = j;
    }
   
    //iterate though, and check last char
    for (int i = 0; i < len1; i++) {
      char c1 = word1.charAt(i);
      for (int j = 0; j < len2; j++) {
        char c2 = word2.charAt(j);
   
        //if last two chars equal
        if (c1 == c2) {
          //update dp value for +1 length
          dp[i + 1][j + 1] = dp[i][j];
        } else {
          int replace = dp[i][j] + 1;
          int insert = dp[i][j + 1] + 1;
          int delete = dp[i + 1][j] + 1;
   
          int min = replace > insert ? insert : replace;
          min = delete > min ? min : delete;
          dp[i + 1][j + 1] = min;
        }
      }
    }
   
    return dp[len1][len2];
  }
  
}
