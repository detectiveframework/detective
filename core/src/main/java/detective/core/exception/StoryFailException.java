package detective.core.exception;

import detective.core.Story;

public class StoryFailException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  private final Story story;
  private final int precentCompleted;

  public StoryFailException(Story story, String message) {
    this(story, 0, message, null);
  }

  public StoryFailException(Story story, String message, Throwable cause) {
    this(story, 0, message, cause);
  }
  
  public StoryFailException(Story story, int precentCompleted, String message, Throwable cause) {
    super(message, cause);
    this.story = story;
    this.precentCompleted = precentCompleted;
  }

  public StoryFailException(Story story, Throwable cause) {
    this(story, 0, cause.getMessage(), cause);
  }

  public Story getStory() {
    return story;
  }

  public int getPrecentCompleted() {
    return precentCompleted;
  }
}
