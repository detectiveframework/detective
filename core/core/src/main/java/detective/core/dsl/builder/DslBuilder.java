package detective.core.dsl.builder;

import groovy.lang.Closure;
import groovy.util.BuilderSupport;
import groovy.util.Expando;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.groovy.runtime.InvokerHelper;

import detective.core.Scenario;
import detective.core.Story;
import detective.core.TestTask;
import detective.core.dsl.DslException;
import detective.core.dsl.SimpleContext;
import detective.core.dsl.SimpleEvents;
import detective.core.dsl.SimpleOutcomes;
import detective.core.dsl.SimpleScenario;
import detective.core.dsl.SimpleStory;

@SuppressWarnings("rawtypes") 
public class DslBuilder extends BuilderSupport{
  
  private Map<SimpleScenario, Map<String, Object>> scenariosParameters = new HashMap<SimpleScenario, Map<String, Object>>();
  private Map<String, Object> getParametersFromScenario(SimpleScenario s){
    Map<String, Object> parameters = scenariosParameters.get(s);
    if (parameters == null){
      parameters = new HashMap<String, Object>();
      scenariosParameters.put(s, parameters);
    }
    
    return scenariosParameters.get(s);
  }
  
  public static final String DATATABLE_PARAMNAME = "datatable";
  
  public static DslBuilder story(){
    return new DslBuilder();
  }
  
  protected Object doInvokeMethod(String methodName, Object name, Object args) {
    Object current = getCurrent();
    if (current != null){
      List list = InvokerHelper.asList(args);
      if (list.size() == 1 && list.get(0) instanceof Closure){
        Closure closure = (Closure)list.get(0);
        if (current instanceof Scenario){
          ScenarioDelegate sub = new ScenarioDelegate(getParametersFromScenario((SimpleScenario)current));
          sub.setScenario((SimpleScenario)current);
          sub.setTitle(name.toString());
          sub.setClosure(closure);
          return sub;
        }else if (current instanceof Story && (methodName.equalsIgnoreCase("share"))){
          StoryDelegate sub = new StoryDelegate(new HashMap<String, Object>());
          sub.story = (Story)current;
          sub.closure = closure;
          closure.setDelegate(sub);
          closure.setResolveStrategy(Closure.DELEGATE_ONLY);
          closure.call();
          
          addStorySharedData(sub, (Story)current, null);
          
          return current;
        }
        
      }
    }
    return super.doInvokeMethod(methodName, name, args);
  }

  @Override
  protected Object createNode(Object name) {
    Object current = getCurrent();
    if (current == null){
      SimpleStory story = new SimpleStory();
      story.setTitle(name.toString());
      return story;
    }else if (current instanceof Story){
      SimpleStory story = (SimpleStory)current;
      SimpleScenario s = new SimpleScenario(story, name.toString());
      story.getScenarios().add(s);
      return s;
    }else if (current instanceof Scenario){
      throw new DslException("Should never reach here.");
    }else if (current instanceof ScenarioDelegate){
      return new Expando();
    }
    return null;
  }

  @Override
  protected Object createNode(Object name, Object value) {
    if (value instanceof SimpleScenario){
      SimpleScenario s = (SimpleScenario)value;
      s.setId(name.toString());
      return value;
    }else if (value instanceof ScenarioDelegate){
      ScenarioDelegate sub = (ScenarioDelegate)value;
      if (name.toString().equalsIgnoreCase("given") || name.toString().equalsIgnoreCase("when")){        
        SimpleContext context = null;
        if (name.toString().equalsIgnoreCase("given")){
          context = new SimpleContext();
          sub.getScenario().addContext(context);
        }else
          context = (SimpleEvents)sub.scenario.getEvents();
        
        context.setTitle(sub.title);
        if (sub.closure != null){
          sub.closure.setResolveStrategy(Closure.DELEGATE_ONLY);
          sub.closure.setDelegate(sub);
          try {
            sub.closure.call();
          }catch (DslException e1) {
            throw e1;
          } catch (Exception e) {
            throw new DslException(e.getMessage() +  ". Please note we have a know ambiguousness for parent child relationship, for example login.username is a valid identifier for us, but when you add login.username.lastname, we have no idea it is going to access a property from identifier login.username or it is a new identifier.", e);
          }
        }
        addContextParameters(sub, context, null);
      }else if (name.toString().equalsIgnoreCase("when")){
        SimpleEvents events = (SimpleEvents)sub.scenario.getEvents();
        events.setTitle(sub.title);
      }else if (name.toString().equalsIgnoreCase("then")){
        SimpleOutcomes outcomes = (SimpleOutcomes)sub.scenario.getOutcomes();
        outcomes.setTitle(sub.title);
        outcomes.setExpectClosure(sub.closure);
      }
      return sub.scenario;
    }else if (getCurrent() == null){
      SimpleStory story = new SimpleStory();
      return story;
    }else if (getCurrent() instanceof Story){
      SimpleStory story = (SimpleStory)getCurrent();
      if (name.toString().equalsIgnoreCase("sothat"))
        story.setPurpose(value.toString());
      else if (name.toString().equalsIgnoreCase("asa"))
        story.setRole(value.toString());
      else if (name.toString().equalsIgnoreCase("inorderto")){
        story.setFeature(value.toString());
      }else if (name.toString().equalsIgnoreCase("iwantto"))
        story.setBenefit(value.toString());
      
      return story;
    }else if (getCurrent() instanceof SimpleScenario && name.toString().equalsIgnoreCase("task")){
      return addTask(name, value);
    }
      
    return null;
  }

  private void addContextParameters(ScenarioDelegate sub, SimpleContext contexts, String propertyPrix) {
    for (Object k : sub.getProperties().keySet()){
      String key = k.toString();
      Object value = sub.getProperty(key);
      
      if (value instanceof ScenarioDelegate){
        this.addContextParameters((ScenarioDelegate)value, contexts, (propertyPrix == null ? "" : propertyPrix + ".") + key);
      }else{
        contexts.addParameter((propertyPrix == null ? "" : propertyPrix + ".") + key, value);
      }
    }
  }
  
  private void addStorySharedData(StoryDelegate sub, Story story, String propertyPrix) {
    for (Object k : sub.getProperties().keySet()){
      String key = k.toString();
      Object value = sub.getProperty(key);
      
      if (value instanceof StoryDelegate){
        this.addStorySharedData((StoryDelegate)value, story, (propertyPrix == null ? "" : propertyPrix + ".") + key);
      }else{
        story.putSharedData((propertyPrix == null ? "" : propertyPrix + ".") + key, value);
      }
    }
  }

  private Object addTask(Object name, Object value) {
    if (value == null)
      throw new DslException("task is null, there is must something wrong in your story config.");
    if (value instanceof TestTask){
      SimpleScenario s = (SimpleScenario)getCurrent();
      s.addTask((TestTask)value);
      return s;
    }else
      throw new DslException("task have to setup as a test task (implement interface TestTask and your class is " + value.getClass().getName() + "), please ask your developer for more details.");
  }

  @Override
  protected Object createNode(Object name, Map attributes) {
    throw new DslException("Unsupported DSL Format : " + name);
  }

  @Override
  protected Object createNode(Object name, Map attributes, Object value) {
    throw new DslException("Unsupported DSL Format : " + name);
  }

  @Override
  protected void setParent(Object parent, Object child) {
    
  }
  
}
