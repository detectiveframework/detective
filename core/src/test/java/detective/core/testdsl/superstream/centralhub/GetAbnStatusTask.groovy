package detective.core.testdsl.superstream.centralhub

import detective.common.json.JacksonMsgConverter;
import detective.core.Parameters
import detective.task.AbstractTask;
import detective.task.HttpClientTask;

class GetAbnStatusTask extends AbstractTask {

	@Override
	protected void doExecute(Parameters config, Parameters output) {
		String fundid = readAsString(config, 'fundid', null, false, 'fundid is missing')
		String centralhubHost = readAsString(config, 'centralhubHost', null, false, 'centralhubHost is missing')

		JacksonMsgConverter converter = new JacksonMsgConverter(false)

		def dto = [:]
		dto.apikey = 'SF'
		dto.aggregatorid = 'haw5lzv5qezyz8mji9z6cvo27kk94yby9c449ozonw3yisypn1k97d2e0472s288'
		dto.list = []
		dto.list << fundid

		config.put('http.post.string', converter.toJson(dto))
		config.put('http.address', "${centralhubHost}/centralhub/rest/getFundStatus")
		HttpClientTask request = new HttpClientTask()
		Parameters result = request.execute(config)
		output.put('httpStatus', result.get('http.status.code'))
	}
}
