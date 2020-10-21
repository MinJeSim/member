
package mileage.external;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Date;

@FeignClient(name="inquiry", url="${api.inquiry.url}")
public interface InquiryService {

    @RequestMapping(method= RequestMethod.POST, path="/inquiries")
    public void cancelInquiry(@RequestBody Inquiry inquiry);

}