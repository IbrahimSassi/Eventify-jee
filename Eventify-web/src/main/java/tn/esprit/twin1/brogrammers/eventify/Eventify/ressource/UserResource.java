package tn.esprit.twin1.brogrammers.eventify.Eventify.ressource;

import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import tn.esprit.twin1.brogrammers.eventify.Eventify.contracts.UserBusinessLocal;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.User;

/*
 *  PS : DON'T FUCKING TOUCH THIS LOVELY BY HAKIM
 */

@Path("user")
@RequestScoped
public class UserResource {

	@EJB
	UserBusinessLocal userBusiness;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void createUser(User user) {

		userBusiness.createUser(user);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("{id}")
	public User findUserById(@PathParam("id") int id) {

		return userBusiness.findUserById(id);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<User> findAllUsers() {

		return userBusiness.findAllUsers();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public void updateUser(User user) {

		userBusiness.updateUser(user);

	}

	@DELETE
	@Path("{id}")
	public void deleteUser(@PathParam("id") int id) {

		userBusiness.deleteUser(id);

	}

}