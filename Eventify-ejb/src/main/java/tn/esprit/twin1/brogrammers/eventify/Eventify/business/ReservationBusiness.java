package tn.esprit.twin1.brogrammers.eventify.Eventify.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import javax.transaction.UserTransaction;

import tn.esprit.twin1.brogrammers.eventify.Eventify.contracts.IReservationBusinessLocal;
import tn.esprit.twin1.brogrammers.eventify.Eventify.contracts.IReservationBusinessRemote;
import tn.esprit.twin1.brogrammers.eventify.Eventify.contracts.ITicketBusinessLocal;
import tn.esprit.twin1.brogrammers.eventify.Eventify.contracts.ITransactionBusinessLocal;
import tn.esprit.twin1.brogrammers.eventify.Eventify.contracts.UserBusinessLocal;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.Event;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.Organization;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.Reservation;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.Ticket;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.Transaction;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.User;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.enumeration.PaymentMethod;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.enumeration.ReservationState;
import tn.esprit.twin1.brogrammers.eventify.Eventify.domain.enumeration.TimerState;

/**
 * Session Bean implementation class ReservationBusiness
 */
@Stateless
public class ReservationBusiness implements IReservationBusinessRemote, IReservationBusinessLocal {

	@PersistenceContext(unitName = "Eventify-ejb")
	EntityManager entityManager;
	@EJB
	UserBusinessLocal userbusiness;

	@EJB
	ITransactionBusinessLocal transactionbusiness;
	@EJB
	ITicketBusinessLocal ticketbusiness;

	@Override
	public void create(Reservation reservation) {
		entityManager.persist(reservation);
		System.out.print(reservation);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Reservation> getAllReservations() {
		List<Reservation> reservation = (List<Reservation>) entityManager
				.createQuery(
						"SELECT new Reservation(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod , user,ticket,r.timerState) "
								+ "FROM Reservation r" + " JOIN r.user user" + " JOIN r.ticket ticket  ")
				.getResultList();

		for (Reservation reservations : reservation) {
			User user = userbusiness.findUserById(reservations.getUser().getId());
			reservations.setUser(user);

			Ticket ticket = ticketbusiness.findTicketById(reservations.getTicket().getId());
			reservations.setTicket(ticket);

			// Transaction transaction =
			// transactionbusiness.findTransactionById(reservations.getTransaction().getId());
			// reservations.setTransaction(transaction);

		}
		return reservation;
	}

	@Override
	public void updateReservation(Reservation reservation) {

		entityManager.merge(reservation);

	}

	@Override
	public boolean deleteReservationById(int id) {
		entityManager.remove(entityManager.find(Reservation.class, id));
		return true;
	}

	@Override
	public Reservation findReservationById(int idReservation) {
		Query query = entityManager.createQuery(
				"SELECT new Reservation" + "(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod) "
						+ "FROM Reservation r WHERE r.id=:idres");

		Reservation r = (Reservation) query.setParameter("idres", idReservation).getSingleResult();
		// r.setUser(userbusiness.findUserById(r.getUser().getId()));
		return r;
	}

	/* MET */

	@Override
	public List<Reservation> findReservationByState(ReservationState reservationState) {
		Query query = entityManager
				.createQuery("SELECT new Reservation"
						+ "(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod)"
						+ " FROM Reservation r " + "WHERE r.reservationState " + "LIKE :reservationState")
				.setParameter("reservationState", reservationState);
		return (List<Reservation>) query.getResultList();
	}

	@Override
	public List<Reservation> findReservationByTimerState(TimerState timerState) {
		Query query = entityManager
				.createQuery("SELECT new Reservation"
						+ "(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod)"
						+ " FROM Reservation r " + "WHERE r.timerState LIKE :timerState")
				.setParameter("timerState", timerState);
		return (List<Reservation>) query.getResultList();
	}

	@Override
	public List<Reservation> findReservationByPaymentMethod(PaymentMethod paymentMethod) {
		Query query = entityManager
				.createQuery("SELECT new Reservation"
						+ "(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod) "
						+ "FROM Reservation r " + "WHERE r.paymentMethod LIKE :paymentMethod")
				.setParameter("paymentMethod", paymentMethod);
		return (List<Reservation>) query.getResultList();
	}

	@Override
	public List<Reservation> findReservationByUserId(int userId) {
		Query query = entityManager.createQuery(
				"SELECT new Reservation" + "(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod)"
						+ " FROM Reservation r " + "WHERE user.id = :userId")
				.setParameter("userId", userId);
		return (List<Reservation>) query.getResultList();
	}

	@Override
	public int CheckConfirmedReservationSum(int idEvent) {
		List<Reservation> list;
		Query query = entityManager
				.createQuery("SELECT new Reservation"
						+ "(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod) FROM Reservation r "
						+ "JOIN r.ticket t JOIN t.event e  WHERE r.reservationState = 'CONFIRMED' AND e.id=:idEvent")
				.setParameter("idEvent", idEvent);
		list = (List<Reservation>) query.getResultList();

		return list.size();
	}

	@Override
	public List<Reservation> getAllReservationGroupByPaymentMethod(int idEvent) {
		Query query = entityManager.createQuery(
				"SELECT new Reservation" + "(r.id,r.amount,r.reservationDate,r.reservationState,r.paymentMethod)"
						+ " FROM Reservation r JOIN r.ticket t JOIN t.event e "
						+ "WHERE e.id = :idEvent GROUP BY r.paymentMethod,r.id")
				.setParameter("idEvent", idEvent);
		return (List<Reservation>) query.getResultList();
	}

	@Override
	public Double getSumOfAmountForOneEvent(int idEvent) {
		Query query1 = entityManager.createQuery("Select SUM(r.amount) from Reservation r JOIN r.ticket t JOIN t.event e WHERE e.id = :idEvent")
				.setParameter("idEvent", idEvent);;
		Double result = (Double) query1.getSingleResult();
		System.out.println("Max Employee Salary :" + result);
		return result;
	}

	@Override
	public Map<String, Long> getAmountOrderByYear() {
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
		TypedQuery<Object[]> q = entityManager.createQuery(
			    "SELECT r.id, count(r.amount) " +
			    "FROM Reservation r  " +
			    "GROUP BY r.paymentMethod,r.id", Object[].class);

			List<Object[]> resultList = q.getResultList();
			Map<String, Long> resultMap = new HashMap<String, Long>(resultList.size());
			for (Object[] result : resultList)
			{System.out.println((String)result[0] + " " + (Long)result[1]);  
			  resultMap.put((String)result[0], (Long)result[1]);
			}
			


	
		
		return null;
	}

	/* MET */
}
