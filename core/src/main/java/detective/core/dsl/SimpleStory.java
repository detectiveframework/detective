package detective.core.dsl;

import groovy.lang.GroovyObjectSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import detective.core.Parameters;
import detective.core.Scenario;
import detective.core.Story;

/**
 *<pre>
http://dannorth.net/whats-in-a-story/
Title (one line describing the story)
 
Narrative:
In Order to [ ]
As a [role]
I want [feature]
So that [benefit]
 
Acceptance Criteria: (presented as Scenarios)
 
Scenario 1: Title
Given [context]
  And [some more context]...
When  [event]
Then  [outcome]
  And [another outcome]...
 
Scenario 2: ...
 *</pre>
 *
 *<pre>
 *Story: Returns go to stock

In order to keep track of stock
As a store owner
I want to add items back to stock when they're returned

Scenario 1: Refunded items should be returned to stock
Given a customer previously bought a black sweater from me
And I currently have three black sweaters left in stock
When he returns the sweater for a refund
Then I should have four black sweaters in stock

Scenario 2: Replaced items should be returned to stock
Given that a customer buys a blue garment
And I have two blue garments in stock
And three black garments in stock.
When he returns the garment for a replacement in black,
Then I should have three blue garments in stock
And two black garments in stock
 *</pre>
 *
 * @author James Luo
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SimpleStory extends GroovyObjectSupport implements Story{
  
  private static final Logger logger = LoggerFactory.getLogger(SimpleStory.class);

  private String title;
  private String role;
  private String feature;
  private String benefit;
  private String purpose;
  
  private final List<Scenario> scenarios = new ArrayList<Scenario>();
  private final List<Scenario> beforeTasks = new ArrayList<Scenario>();
  private final List<Scenario> afterTasks = new ArrayList<Scenario>();
  
  private final Parameters sharedDataMap = new ParametersImpl();
  
  //TODO Readonly
  public SimpleStory(){
    
  }
  
  
  @Override
  public String toString() {
    return "story \"" + title + "\" {\n As a " + role + "\n I want " + feature + "\n So that "
        + benefit + "\n\n " + scenarios + "\n}";
  }
  
//  public Object getProperty(final String property) {
//    return super.getProperty(property);
//  }
//  
//  public void setProperty(String property, Object newValue) {
//    super.setProperty(property, newValue);
//  }
//
//  public Object invokeMethod(String methodName, Object args) {
//    return super.invokeMethod(methodName, args);
//  }
  
  /* (non-Javadoc)
   * @see detective.core.AStory#getTitle()
   */
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  /* (non-Javadoc)
   * @see detective.core.AStory#getRole()
   */
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  /* (non-Javadoc)
   * @see detective.core.AStory#getFeature()
   */
  public String getFeature() {
    return feature;
  }

  public void setFeature(String feature) {
    this.feature = feature;
  }

  /* (non-Javadoc)
   * @see detective.core.AStory#getBenefit()
   */
  public String getBenefit() {
    return benefit;
  }

  public void setBenefit(String benefit) {
    this.benefit = benefit;
  }

  /* (non-Javadoc)
   * @see detective.core.AStory#getScenarios()
   */
  public List<Scenario> getScenarios() {
    return scenarios;
  }


  public String getPurpose() {
    return purpose;
  }


  public void setPurpose(String purpose) {
    this.purpose = purpose;
  }


  public Parameters getSharedDataMap() {
    return sharedDataMap;
  }

  public void putSharedData(String key, Object value) {
    if (! sharedDataMap.containsKey(key)){
      logger.info("A new shared data [" + key + "] created for story [" + title + "]");
      if (value == null)
        value = new SharedVariableImpl(this, key);

      sharedDataMap.put(key, value);
    }else{
      Object oldValue = sharedDataMap.getUnwrappered(key);
      try {
        if (oldValue instanceof SharedVariable)
          sharedDataMap.put(key, value);
        else
          //throw new DslException("Shared data [" + key + "] can only setup once in story [" + title + "]");
          logger.info("As shared data [" + key + "] can only setup once in story [" + title + "], your value ignored");
      } catch (Exception e) {
        throw new DslException(e.getMessage() + " current story [" + title + "]", e);
      }
    }
  }


  public List<Scenario> getBeforeTasks() {
    return ImmutableList.copyOf(this.beforeTasks);
  }


  public List<Scenario> getAfterTasks() {
    return ImmutableList.copyOf(this.afterTasks);
  }
  
  
}
