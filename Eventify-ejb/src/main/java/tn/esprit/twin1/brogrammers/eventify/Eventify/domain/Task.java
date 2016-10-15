package tn.esprit.twin1.brogrammers.eventify.Eventify.domain;

import java.io.Serializable;
import java.lang.String;
import java.util.Date;
import java.util.List;

import javax.persistence.*;



/**
 * Entity implementation class for Entity: Task
 *
 */
@Entity
public class Task implements Serializable {

	
	private int id;
	private String taskTitle;
	private String taskDescription;
	private int taskStatus;
	private Date createdAt;
	private static final long serialVersionUID = 1L;
	private List<Organizer> organizers;
	private Event event;

	public Task() {
		super();
	} 
	@Id
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}   
	public String getTaskTitle() {
		return this.taskTitle;
	}

	public void setTaskTitle(String taskTitle) {
		this.taskTitle = taskTitle;
	}   
	public String getTaskDescription() {
		return this.taskDescription;
	}

	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}   
	public int getTaskStatus() {
		return this.taskStatus;
	}

	public void setTaskStatus(int taskStatus) {
		this.taskStatus = taskStatus;
	}   
	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	@OneToMany(mappedBy="task")
	public List<Organizer> getOrganizers() {
		return organizers;
	}
	public void setOrganizers(List<Organizer> organizers) {
		this.organizers = organizers;
	}
	
	@ManyToOne
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	
	
   
}