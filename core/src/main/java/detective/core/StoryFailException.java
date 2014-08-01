package detective.core;

public class StoryFailException extends RuntimeException {
  
  private static final long serialVersionUID = 1L;
  
  private final Story story;

  public StoryFailException(Story story, String message) {
    super(message);
    this.story = story;
  }

  public StoryFailException(Story story, String message, Throwable cause) {
    super(message, cause);
    this.story = story;
  }

  public StoryFailException(Story story, Throwable cause) {
    super(cause);
    this.story = story;
  }

  public Story getStory() {
    return story;
  }
}
