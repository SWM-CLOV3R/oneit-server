package clov3r.oneit_server;

import clov3r.oneit_server.response.BaseResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Test API")
public class TestController {

    @GetMapping("/hello")
    public BaseResponse Test() {
        String result = "Hello One!t";
        return new BaseResponse(result);
    }

}
