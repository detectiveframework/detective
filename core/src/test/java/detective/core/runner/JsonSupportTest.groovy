package detective.core.runner;

import static org.junit.Assert.*;
import static detective.core.Detective.*;
import static detective.core.Matchers.*;
import detective.core.dsl.DslException
import detective.core.exception.StoryFailException;
import detective.core.task.JsonBuilderTask
import detective.core.TestTaskFactory;
import detective.core.Story
import detective.core.StoryRunner

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JsonSupportTest {

  @Test
  public void testJsonMap(){
    def json = jsonBuilder().person{
      name "Guillaume"
      age 33
      pets "dog", "cat"
    }
    
    assert json instanceof Map;
    assert json.person.name == "Guillaume"
    assert json.person.age == 33
    assert json.person.pets.size() == 2
    assert json.person.pets[0] == "dog"
    assert json.person.pets[1] == "cat"
  }
  
  @Test
  public void testJsonBuilder(){
    def builder = jsonBuilder(){
      person{
        name "Guillaume"
        age 33
        pets "dog", "cat"
      }
    }
    
    assert builder.content instanceof Map;
    assert builder.toString() == '{"person":{"name":"Guillaume","age":33,"pets":["dog","cat"]}}'
  }
  
  @Test
  public void testJson() {
    story() "Direct read json" {

      scenario "json" {
        given "give json input" {
          //'{"person":{"name":"Guillaume","age":33,"pets":["dog","cat"]}}'
          jsoninput = jsonBuilder(){
            person{
              name "Guillaume"
              age 33
              pets "dog", "cat"
            }
          }
        }

        when "run json builder task" { 
          runtask new JsonBuilderTask(); 
        }

        then "parameter should send back"{
          json.person.name << "Guillaume"
          json.person.age << 33
          json.person.pets.size() << 2
          json.person.pets[0] << "dog"
          json.person.pets[1] << "cat"
        }
      }
    }
  }
  
}
