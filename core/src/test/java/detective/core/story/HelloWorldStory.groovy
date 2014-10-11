package detective.core.story

import static detective.core.Detective.*;

story() "Demostrate hello world" {
  scenario_hello {
    step1 "print out hello world"{
      println "Hello World"
    }
  }
}

story() "Demostrate test string concat" {
  scenario_hello {
    give "word Hello"{
      word1 = "Hello"
    }
    give "word World"{
      word2 = "World";
    }
    when "run String.concat"{
      helloworld = word1.concat(word2)
    }
    then "word1 and word2 should concated"{
      helloworld << "HelloWorld"
    }
  }
}