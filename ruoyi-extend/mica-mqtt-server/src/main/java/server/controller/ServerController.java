package server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import server.service.ServerService;

@RequiredArgsConstructor
@RequestMapping("/mqtt/server")
@RestController
public class ServerController {
	private final ServerService service;

	@PostMapping("publish")
	public boolean publish(@RequestBody String body) {
		return service.publish(body);
	}

}
