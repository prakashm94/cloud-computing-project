package neu.coe.project.cloudapp.Model;


import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "attachment", schema = "login_aws")
public class Attachment {

    @Id
    @Column(name = "attachment_id")
    private String aid;

     @Column(name = "url")
    private String url;

    public Attachment(){
        aid= UUID.randomUUID().toString();
    }

    public String getId() {
        return aid;
    }

    public void setId(String aid) {
        this.aid = aid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
