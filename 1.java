abstract class AbstractAssessmentFlow {

    public final void executeAssessment() {
        validate();        
        prepare();         
        evaluate();        
        publishResult();   
    }

  
    protected void validate() {
        System.out.println("Validating candidate identity, hall ticket, rules...");
    }

    protected void prepare() {
        System.out.println("Preparing environment, loading question bank...");
    }

  
    protected abstract void evaluate();

    protected void publishResult() {
        System.out.println("Publishing result to university portal...");
    }
}

interface AutoAssessment{
    default void evaluate(){
           System.out.println("Auto Evaluation :I + Objective Answer Matching");
    }
    
}
interface ManualAssessment{
    default void evaluate(){
          System.out.println("Manual Evaluation → Faculty checking subjective answers");
    }
    
}

class UnifiedAssessmentExecutor extends AbstractAssessmentFlow implements  AutoAssessment,ManualAssessment{
     private boolean isProctored;   

    public UnifiedAssessmentExecutor(boolean isProctored) {
        this.isProctored = isProctored;
    }

    

    protected void evaluate() {

        if (isProctored) {
           
            ManualAssessment.super.evaluate();
        } else {
        
            AutoAssessment.super.evaluate();
        }
    }
    
}
