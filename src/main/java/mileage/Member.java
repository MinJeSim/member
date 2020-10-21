package mileage;

import javax.persistence.*;

import org.springframework.beans.BeanUtils;

import java.util.List;

@Entity
@Table(name = "Member_table")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long memberId;
    private String phoneNo;
    private String nickname;
    private String memberStatus;
    private String inquiryStatus;

    @PrePersist
    public void onPrePersist() {
        if (this.getMemberStatus().equals("CANCEL")) {
            InquiryCancel inquiryCancel = new InquiryCancel();
            BeanUtils.copyProperties(this, inquiryCancel);
            inquiryCancel.publishAfterCommit();

            //Following code causes dependency to external APIs
            // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

            mileage.external.InquiryHst inquiryHst = new mileage.external.InquiryHst();
            // mappings goes here
            inquiryHst.setMemberId(this.getMemberId());
            inquiryHst.setInquiryStatus("CANCEL");

            MemberApplication.applicationContext.getBean(mileage.external.InquiryService.class).cancel(inquiryHst);
        }
    }

    @PostPersist
    public void onPostPersist() {
        switch (this.getMemberStatus()) {
            case "READY":
                MemberJoined memberJoined = new MemberJoined();
                BeanUtils.copyProperties(this, memberJoined);
                memberJoined.setMemberStatus("READY");
                memberJoined.publishAfterCommit();

                break;
            case "WITHDRAWN":
                mileage.external.Forfeiture forfeiture = new mileage.external.Forfeiture();
                forfeiture.setId(memberId);
                forfeiture.setRemainPoint(0L);

                MemberApplication.applicationContext.getBean(mileage.external.ForfeitureService.class).forfeitHstInsert(forfeiture);

                break;
            case "INQUIRING":
                InquirySent inquirySent = new InquirySent();

                BeanUtils.copyProperties(this, inquirySent);
                inquirySent.setMemberId(this.getMemberId());
                inquirySent.setInquiryContents("DO TEST");

                inquirySent.publishAfterCommit();
                break;
        }
    }

    @PostUpdate
    public void onPostUpdate() {
        if (this.inquiryStatus == null) {
            MemberStatusChanged memberStatusChanged = new MemberStatusChanged();
            BeanUtils.copyProperties(this, memberStatusChanged);
            memberStatusChanged.publishAfterCommit();
        } else {
            InquiryStatusUpdated inquiryStatusUpdated = new InquiryStatusUpdated();
            BeanUtils.copyProperties(this, inquiryStatusUpdated);
            inquiryStatusUpdated.publishAfterCommit();
        }
    }

    @PreRemove
    public void onPreRemove() {
        MemberWithdrawn memberWithdrawn = new MemberWithdrawn();
        BeanUtils.copyProperties(this, memberWithdrawn);
        memberWithdrawn.setMemberStatus("WITHDRAWAL");
        memberWithdrawn.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.
        mileage.external.Point point = new mileage.external.Point();
        // mappings goes here

        point.setMemberId(this.getMemberId());
        point.setMemberStatus("WITHDRAWAL");

        MemberApplication.applicationContext.getBean(mileage.external.PointService.class).forfeit(point, id);

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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getInquiryStatus() {
        return inquiryStatus;
    }

    public void setInquiryStatus(String inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }
}
