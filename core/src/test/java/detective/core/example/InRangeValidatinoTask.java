package detective.core.example;

import java.math.BigInteger;

import detective.core.Parameters;
import detective.core.config.ConfigException;
import detective.task.AbstractTask;

/**
 * Validates a give number is within a range
 * 
 * 
 * <h3>Input</h3>
 * 
 * <pre>
 *   number: the value to check against the range
 *   begin: the beginning of the range
 *   end: the end of the range
 * </pre>
 * 
 * <h3>Output</h3>
 * 
 * <pre>
 *   inRange: boolean to indicate number is within the range
 * </pre>
 */

public class InRangeValidatinoTask extends AbstractTask {

	@Override
	protected void doExecute(Parameters config, Parameters output) {

		if (config == null)
			throw ConfigException.configCantEmpty();
		else if (config.get("number") == null)
			throw new ConfigException(
					"The value to check against the range cannot be empty!");
		else if (config.get("begin") == null)
			throw new ConfigException(
					"The value to set the beginning of the range cannot be empty!");
		else if (config.get("end") == null)
			throw new ConfigException(
					"The value to set the end of the range cannot be empty!");

		BigInteger number = new BigInteger(config.get("number").toString());
		BigInteger begin = new BigInteger(config.get("begin").toString());
		BigInteger end = new BigInteger(config.get("end").toString());

		if (number.compareTo(begin) == 1 && number.compareTo(end) == -1)
			output.put("inRange", true);
		else
			output.put("inRange", false);

	}

}
