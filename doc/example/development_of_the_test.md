# Development of the Test

Below task needs to be developed by the developer in order to test the functionality of the implemented/modified code.

```groovy
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
```
#### # Please note the above test is only for the demonstration purpose. Insted of implementing the validation code inside the Task, code above should refer to the exact point of the code that performs the validation.
