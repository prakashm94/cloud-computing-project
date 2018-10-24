package neu.coe.project.cloudapp.Repository;

import neu.coe.project.cloudapp.Model.Attachment;
import neu.coe.project.cloudapp.Model.TransactionData;
import org.springframework.data.repository.CrudRepository;

public interface AttachmentRepository extends CrudRepository<Attachment, String> {
}
