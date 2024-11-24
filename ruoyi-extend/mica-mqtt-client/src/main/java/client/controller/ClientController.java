package client.controller;

import client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/mqtt/client")
@RestController
public class ClientController {

	private final ClientService service;

	@PostMapping("/publish")
	public boolean publish(@RequestBody String body) {
		return service.publish(body);
	}

	@GetMapping("/sub")
	public boolean sub() {
		return service.sub();
	}

}
