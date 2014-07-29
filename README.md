# The Bug Detective

## Introduction

* Test specification is the Code, always keep running so that never outdated
* Make more sense as ProjectManager/BA/Testers able to write story or case by theirselves
* Armed to simplify the integration, user acceptance and pressure test
* Armed to run the tests in development / test / live environment with 24*7
* Building parallel support
* Real-time reporting so that you know your web site is servicing what they should service
* History comparison between your old (pressure) and new testers
* Able to wrapper into your junit (or *unit) test

Able to support all platform as

* A DSL which fade out the language barrier
* Built in Tasks should able to support different languages, for example WebDriver tasks 
* Extend task system via dynamic languages for example Groovy, Scala, Ruby, JS, etc...
* Able to run in all major platforms, testing in Linux, Mac and Windows

Clearly define the test tasks tester and developer need do. Easy for tester to use, easy for developer to extend.

A Task: Developer defined how to perform a test, for example login can be a task, it ask tester supply username and password and return if it's successful or not.
A Step: Tester define the input data and expected output data along with A Task, for example a step "login" will use "login" task defined by developer but test define the input data.
A Story: Combination of steps, defined by Testers

### The system need:
Extensible: Developers should easy to extend the test task
Easy: Testers should easy to understand how to build a story (which is combination of tasks)
  - Task need drive by document
  - Create Document for all tasks automatically

### Testers:
  - The test stories
  - Input Test Data
  - Expected Output Data

### Developers:
  Define the task.
  For every task:
    define the input data
    define output data
    do the check for output data if possible

### The Detective
  Provider a way that PM/BA/Testers create a story.
  A story contains steps and expected output data for every step.
  Schedule and run the steps parallel
  
### Discussion 
  Config (for example YAML) or Groovy DSL?
  if config changed by tester, how developers know?
  we can make a config runnable
  DSL got ide support, you can put break point, you can even write your code, very powerful  

### What's the story looks like

	story() "Returns go to stock" {
      inOrderTo "keep track of stock"
      asa "store owner"
      iwantto "add items back to stock when they're returned"
      sothat "..."
      
      scenario_refund "Refunded items should be returned to stock" {
        task StockTaskFactory.stockManagerTask()
      
        given "a customer previously bought a black sweater from me" {
          
        }
        
        given "I currently have three black sweaters left in stock" {
          sweater.black = 3
          sweater.blue = 0
        }
        
        when "he returns the sweater for a refund" {
          sweater.refund.black = 1
        }
        
        then "I should have four black sweaters in stock"{
          sweater.refund.black << equalTo(1)
          sweater.black << equalTo(4)
        }
      }
    }
  
## Roadmap

  - Built in chaos monkey for running tests
  - Web UI for write stories
  - Parallel
  - (Cloud) runner
  - Hamcrest matchers
  - DDD
  - BDD
  - Docker support
  - Web Security Test
  - Web UI for write tasks
  - Global Distributed Cloud Runner
  - Junit integration
  
  
## Installation

Install 

## Configuration


## Troubleshooting/Issues

  
## FAQ

Q. Q1?

A. A1  
  
## Developer's Guide

What's the task looks like
  - Input Data
  - Do the real job (post the data and get return)
  - Verify the data if possible
  - Return the data to tester

  login {in ->
    def result = httpPost({
    		in.url = "${domain}/login_check"
    	});
    assert result.statusCode == 200;
    return result;
  }  
  
## Articles
	http://david.heinemeierhansson.com/2014/tdd-is-dead-long-live-testing.html
	//advances in parallelization and cloud runner infrastructure
	
	http://mike.ucoz.com/publ/programming/ubuntu/selenium_ubuntu_amazon_ec2_headless/8-1-0-4
  
## Great Other Framworks:
	hamcrest(http://hamcrest.org/)
	Guiceberry(https://code.google.com/p/guiceberry/)
	GSpec
	EasyB
	Spock (http://docs.spockframework.org/)
	jbehave
	https://github.com/jnicklas/capybara
	http://phantomjs.org/
	https://github.com/detro/ghostdriver
	
	
	
  