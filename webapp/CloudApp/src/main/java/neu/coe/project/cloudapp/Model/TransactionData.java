package neu.coe.project.cloudapp.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "TransactionData", schema = "login_aws")
public class TransactionData {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(name = "id")
    private String id;

    @Column(name = "description")
    private String description;
    @Column(name = "merchant")
    private String merchant;
    @Column(name = "amount")
    private String amount;
    @Column(name = "category")
    private String category;
    @Column(name = "date")
    private String date;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "username", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private UserData userData;

    public TransactionData(){
        id=UUID.randomUUID().toString();
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
//UUID.randomUUID().toString()
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMerchant() {
        return merchant;
    }

    public void setMerchant(String merchant) {
        this.merchant = merchant;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
