package detective.core.story.superstream.centralhub

import static detective.core.Detective.*
import detective.core.TestTaskFactory


story() "Get abn status from centralhub Story" {
  inOrderTo "retrieve abn status"
  
  scenario "for stress test" {
    given "random fundid" {
			centralhubHost = 'http://localhost:8080'
      fundid = UUID.randomUUID().toString().replaceAll('-', '')
    }
    
    when "run task"{
      runtask TestTaskFactory.getAbnStatusTask();
    }
		
		then 'returned http status 200' {
			httpStatus << '200'
		}
    
  }
}

