package detective.core.runner;

import detective.core.Story;
import detective.core.dsl.builder.DslBuilder;

public class DslBuilderAndRun extends DslBuilder {
  
  protected Object doInvokeMethod(String methodName, Object name, Object args){
    Object obj = super.doInvokeMethod(methodName, name, args);
    if (obj instanceof Story){
      Story story = (Story)obj;
      if (story.getTitle() != null && story.getTitle().equals(methodName)){
        doFinishedBuilding(story);
      }
      
    }
    return obj;
  }
  
  protected Object doFinishedBuilding(Story story){
    new SimpleStoryRunner().run(story);
    return story;
  }

}
