package clov3r.oneit_server;

import clov3r.oneit_server.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/hello")
    public BaseResponse Test() {
        String result = "Hello One!t";
        return new BaseResponse(result);
    }

}
