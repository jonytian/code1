package com.legaoyi.message.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.legaoyi.common.util.HttpClientUtil;
import com.legaoyi.common.util.JsonUtil;
import com.legaoyi.common.util.Result;
import com.legaoyi.management.model.Dictionary;
import com.legaoyi.persistence.jpa.service.GeneralService;
import com.legaoyi.platform.rest.BaseController;

/**
 * @author gaoshengbo
 */
@RestController("vedioController")
@RequestMapping(value = "/vedio", produces = { "application/json" })
public class VedioController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(VedioController.class);

	@Autowired
	@Qualifier("generalService")
	private GeneralService service;

	@GetMapping(value = "/inactive/streams")
	public Result getInactiveStreams() throws Exception {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Map<String, Object> andCondition = new HashMap<String, Object>();
			andCondition.put("code.eq", "1078-video-stats");
			andCondition.put("type.eq", 90);
			List<?> dictionarys = this.service.find(Dictionary.ENTITY_NAME, null, false, andCondition);
			if (dictionarys == null || dictionarys.isEmpty()) {
				return new Result(list);
			}
			Dictionary dictionary = (Dictionary) dictionarys.get(0);
			Map<?, ?> data = JsonUtil.convertStringToObject(dictionary.getContent(), Map.class);
			String url = (String) data.get("url");
			String json = HttpClientUtil.send(url);
			data = JsonUtil.convertStringToObject(json, Map.class);
			if (data != null) {
				data = (Map<?, ?>) data.get("http-flv");
				List<?> servers = (List<?>) data.get("servers");
				if (servers != null) {
					for (Object o : servers) {
						Map<?, ?> server = (Map<?, ?>) o;
						List<?> applications = (List<?>) server.get("applications");
						if (applications != null) {
							for (Object o1 : applications) {
								Map<?, ?> application = (Map<?, ?>) o1;
								Map<?, ?> live = (Map<?, ?>) application.get("live");
								if (live != null) {
									List<?> streams = (List<?>) live.get("streams");
									if (streams != null) {
										for (Object o2 : streams) {
											Map<?, ?> stream = (Map<?, ?>) o2;
											String active = String.valueOf(stream.get("active"));
											String publishing = String.valueOf(stream.get("publishing"));
											String name = (String) stream.get("name");
											String[] arr = name.split("_");
											String simCode = arr[0];
											while (simCode.length() < 12) {
												simCode = "0" + simCode;
											}
											int channelId = Integer.parseInt(arr[2]);
											int nclients = Integer.valueOf(String.valueOf(stream.get("nclients")));
											if ((active.equals("false") || publishing.equals("false"))
													&& nclients > 0) {
												Map<String, Object> map = new HashMap<String, Object>();
												map.put("simCode", simCode);
												map.put("channelId", channelId);
												list.add(map);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("", e);
		}

		if (!list.isEmpty()) {
			logger.warn("*******设备推流异常，list={}", JsonUtil.covertObjectToString(list));
		}

		return new Result(list);
	}
}
