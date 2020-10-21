package mileage;

public class InquiryCancel extends AbstractEvent {

    private Long id;
    private Long memberId;

    public InquiryCancel(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getMemberId() {
        return memberId;
    }
    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
