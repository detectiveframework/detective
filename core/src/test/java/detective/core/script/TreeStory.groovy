package detective.core.script

import static detective.core.Detective.*;
import static detective.core.Matchers.*;

import detective.core.TestTaskFactory;

story() "Tree Stroy " {
  inOrderTo "create a tree like story structure"
  iWantTo "A story should able to include other script like stories"
  version "1.0.1" "1.1.1"
  author "James Luo"
  
  share {
    shared.toplevel.novalue
    shared.toplevel.valueassigned = "TopLevel Assigned Value"
  }
  
  //We support only one way to point out where is the story you want include into
  include "tree.include.SimpleStory1"
  
  //scenarios should able to leave as blank if a story include other story
}