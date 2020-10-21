package mileage.external;

public class Inquiry {

    private Long id;
    private Long memberId;
    private String inquiryStatus;
    private String inquiryContents;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getInquiryStatus() {
        return inquiryStatus;
    }
    public void setInquiryStatus(String inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
    public String getInquiryContents() {
        return inquiryContents;
    }
    public void setInquiryContents(String inquiryContents) {
        this.inquiryContents = inquiryContents;
    }

}
