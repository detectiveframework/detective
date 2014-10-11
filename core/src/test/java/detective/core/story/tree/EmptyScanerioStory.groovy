package detective.core.story.tree

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;

story() "Empty Scanerio Story" {
  desc "Empty scanerio story should result a error if this story don't have child story"
  whatHappened "A EmptyStoreException should throw up if story has no scenario and no child story"
  
  //We support only one way to point out where is the story you want include into
  include "tree.include.SimpleStory1"
}
