package br.com.springbatch.steps;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;

import br.com.springbatch.model.Line;

/**
 * classe respons�vel por calcular a idade de cada pessoa no arquivo.
 *
 */
public class LinesProcessor implements Tasklet, StepExecutionListener {

	private static final Logger logger = Logger.getLogger(LinesProcessor.class.getName());
	
	private List<Line> lines;
	
	@Override
	public void beforeStep(StepExecution stepExecution) {
		
		ExecutionContext executionContext = stepExecution.getJobExecution().getExecutionContext();
        this.lines = (List<Line>) executionContext.get("lines");
        logger.info("Lines Processor initialized.");
	}
	
	/*
	 * Este m�todo � onde se adiciona a l�gica para cada etapa. 
	 */
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		
		for (Line line : lines) {
            long age = ChronoUnit.YEARS.between(line.getLocalDate(), LocalDate.now());
            logger.info("Calculated age " + age + " for line " + line.toString());
            line.setAge(age);
        }
		
        return RepeatStatus.FINISHED;
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		logger.info("Lines Processor ended.");
        
		return ExitStatus.COMPLETED;
	}

}
