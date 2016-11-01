package tn.esprit.twin1.brogrammers.eventify.Eventify.contracts;

import java.util.List;

import javax.ejb.Remote;

import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.Event;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.ReferrelUser;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.User;

@Remote
public interface RefferUserBusinessRemote {
	
	public void ChooseReferred(ReferrelUser Reffered);
	
	public List<ReferrelUser> FindReferredsByIdReferral(int idReferral);
	
	public User FindReferralByIdReferred(int idReferred);
	
	public boolean RemoveReferred(int idReferred);
	
	public List<User> FindAllReferrals();
	
	public void updateReffered(User Referred);
	
	public List<ReferrelUser> FindAll();

}
