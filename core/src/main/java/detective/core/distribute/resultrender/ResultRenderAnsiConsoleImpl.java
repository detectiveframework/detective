package detective.core.distribute.resultrender;

import static org.fusesource.jansi.Ansi.ansi;
import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;

import java.util.List;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import detective.core.distribute.JobRunResult;
import detective.utils.Utils;

public class ResultRenderAnsiConsoleImpl implements ResultRender {

  @Override
  public void render(List<JobRunResult> results, long timeElapsedSec) {
    AnsiConsole.systemInstall();
    try{
      createAnsiCode(results);
    }finally{
      AnsiConsole.systemUninstall();
    }    
  }
  
  void createAnsiCode(List<JobRunResult> results){
    for (JobRunResult result : results)
      System.out.println(createAnsiCode(result));
  }
  
  Ansi createAnsiCode(JobRunResult result){
    Ansi ansi = ansi();
    
    if (result.isIgnored()){
      ansi.fg(BLUE);
    }else if (result.getSuccessed())
      ansi.fg(GREEN);
    else
      ansi.fg(RED);
      
    ansi.bold().a("Story Name: ").a(result.getStoryName()).boldOff()
      .a("\n").bold().a("| -- Scenario Name: ").boldOff().a(result.getScenarioName());
    
    if (result.isIgnored()){
      ansi.a("\n").bold().a("| -- Ignored:       Yes").boldOff();
    }else{
      ansi.a("\n").bold().a("| -- Successed:     ").boldOff().a(result.getSuccessed() ? "Yes" : "Failed" );
    }
      
    ansi.a("\n");
    
    if (result.getError() != null){
      ansi.bold().a("| -- Error:         ").a(result.getError().getMessage()).boldOff().a("\n");
      ansi.bold().a("| -- Error Callstack:").boldOff().a(Utils.getStackTrace(result.getError())).a("\n");
    }

    ansi.reset();
    
    return ansi;
  }
  
  void println(String msg){
    System.out.println(msg);
  }

}
