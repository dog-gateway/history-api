/**
 * 
 */
package it.polito.elite.dog.communication.rest.history.api;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author bonino
 * 
 *         http://localhost/api/v1/history/devices/{device-id}/notifications/{
 *         notification-type}/{notification-name}/{notification-params}?filter
 * 
 *         all -> special id identifying the collection
 */
@Path("/api/v1/history/")
public interface HistoryRESTApi
{
	/**
	 * Provides the history of all parametric notifications associated to the
	 * given device, identified by the given device id.
	 * 
	 * @param deviceId
	 *            The URI of the device for which the history should be
	 *            extracted.
	 * @param startDate
	 *            The date from which starting to extract the history, if not
	 *            given is set to the EPOCH value.
	 * @param endDate
	 *            The date at which extraction should end, if not given is set
	 *            to NOW by default.
	 * @param offset
	 *            The offset from which returned results should start (0 by
	 *            default)
	 * @param limit
	 *            The maximum number of result to return (UNLIMITED by default)
	 * 
	 * @return The stored notifications, in JSON
	 */
	@GET
	@Path("/devices/{device-id}/notifications/parametric/all")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllDeviceParametricNotifications(
			@PathParam("device-id") String deviceId,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	/**
	 * Provides the history of all non parametric notifications associated to
	 * the given device, identified by the given device id.
	 * 
	 * @param deviceId
	 *            The URI of the device for which the history should be
	 *            extracted.
	 * @param startDate
	 *            The date from which starting to extract the history, if not
	 *            given is set to the EPOCH value.
	 * @param endDate
	 *            The date at which extraction should end, if not given is set
	 *            to NOW by default.
	 * @param offset
	 *            The offset from which returned results should start (0 by
	 *            default)
	 * @param limit
	 *            The maximum number of result to return (UNLIMITED by default)
	 * @param aggregate
	 *            The aggregation flag. If true all notifications are aggregated
	 *            in a single event stream, otherwise one stream per
	 *            notification will be generated. (default, true)
	 * @return
	 */
	@GET
	@Path("/devices/{device-id}/notifications/nonparametric/all")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllDeviceNonParametricNotifications(
			@PathParam("device-id") String deviceId,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("aggregate") Boolean aggregate);

	/**
	 * Provides the history of all continuous states associated to the given
	 * device, identified by the given device id.
	 * 
	 * @param deviceId
	 *            The URI of the device for which the history should be
	 *            extracted.
	 * @param startDate
	 *            The date from which starting to extract the history, if not
	 *            given is set to the EPOCH value.
	 * @param endDate
	 *            The date at which extraction should end, if not given is set
	 *            to NOW by default.
	 * @param offset
	 *            The offset from which returned results should start (0 by
	 *            default)
	 * @param limit
	 *            The maximum number of result to return (UNLIMITED by default)
	 * 
	 * @return The stored notifications, in JSON
	 */
	@GET
	@Path("/devices/{device-id}/states/continuous/all")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllDeviceContinuousStates(
			@PathParam("device-id") String deviceId,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	/**
	 * Provides the history of all discrete states associated to the given
	 * device, identified by the given device id.
	 * 
	 * @param deviceId
	 *            The URI of the device for which the history should be
	 *            extracted.
	 * @param startDate
	 *            The date from which starting to extract the history, if not
	 *            given is set to the EPOCH value.
	 * @param endDate
	 *            The date at which extraction should end, if not given is set
	 *            to NOW by default.
	 * @param offset
	 *            The offset from which returned results should start (0 by
	 *            default)
	 * @param limit
	 *            The maximum number of result to return (UNLIMITED by default)
	 * @param aggregate
	 *            The aggregation flag. If true all notifications are aggregated
	 *            in a single event stream, otherwise one stream per
	 *            notification will be generated. (default, true)
	 * @return
	 */
	@GET
	@Path("/devices/{device-id}/states/discrete/all")
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllDeviceDiscreteStates(
			@PathParam("device-id") String deviceId,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit,
			@QueryParam("aggregate") Boolean aggregate);

	@GET
	@Path("/devices/{device-id}/notifications/parametric/{notification-name}/{notification-params}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDeviceSpecificParametricNotification(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			@PathParam("notification-params") String notificationParams,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/devices/{device-id}/notifications/parametric/{notification-name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDeviceSpecificParametricNotification(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/devices/{device-id}/notifications/nonparametric/{notification-name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDeviceSpecificNonParametricNotification(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/devices/{device-id}/states/continuous/{state-name}/{state-params}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDeviceSpecificContinuousStates(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			@PathParam("state-params") String stateParams,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/devices/{device-id}/states/continuous/{state-name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDeviceSpecificContinuousStates(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);

	@GET
	@Path("/devices/{device-id}/states/discrete/{state-name}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getDeviceSpecificDiscreteStates(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@QueryParam("offset") Integer offset,
			@QueryParam("limit") Integer limit);
	
	// -------------- CSV PUT -------------------------

	@PUT
	@Path("/devices/{device-id}/notifications/parametric/{notification-name}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void insertSpecificDeviceParametricNotificationCSV(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			String csvData);

	@PUT
	@Path("/devices/{device-id}/notifications/parametric/{notification-name}/{notification-params}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void insertSpecificDeviceParametricNotificationCSV(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			@PathParam("notification-params") String notificationParams,
			String csvData);
	
	@PUT
	@Path("/devices/{device-id}/notifications/nonparametric/{notification-name}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void insertSpecificDeviceNonParametricNotificationCSV(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			String csvData);
	
	@PUT
	@Path("/devices/{device-id}/states/continuous/{state-name}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void insertSpecificDeviceContinuousStateCSV(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			String csvData);

	@PUT
	@Path("/devices/{device-id}/states/continuous/{state-name}/{state-params}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void insertSpecificDeviceContinuousStateCSV(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			@PathParam("state-params") String stateParams,
			String csvData);
	
	@PUT
	@Path("/devices/{device-id}/states/discrete/{state-name}")
	@Consumes(MediaType.TEXT_PLAIN)
	public void insertSpecificDeviceDiscreteStateCSV(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			String csvData);
	
	// ------------ JSON PUT ------------------
	
	@PUT
	@Path("/devices/{device-id}/notifications/parametric/{notification-name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertSpecificDeviceParametricNotificationJSON(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			String jsonData);

	@PUT
	@Path("/devices/{device-id}/notifications/parametric/{notification-name}/{notification-params}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertSpecificDeviceParametricNotificationJSON(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			@PathParam("notification-params") String notificationParams,
			String jsonData);
	
	@PUT
	@Path("/devices/{device-id}/notifications/nonparametric/{notification-name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertSpecificDeviceNonParametricNotificationJSON(
			@PathParam("device-id") String deviceId,
			@PathParam("notification-name") String notificationName,
			String jsonData);
	
	@PUT
	@Path("/devices/{device-id}/states/continuous/{state-name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertSpecificDeviceContinuousStateJSON(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			String jsonData);

	@PUT
	@Path("/devices/{device-id}/states/continuous/{state-name}/{state-params}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertSpecificDeviceContinuousStateJSON(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			@PathParam("state-params") String stateParams,
			String jsonData);
	
	@PUT
	@Path("/devices/{device-id}/states/discrete/{state-name}")
	@Consumes(MediaType.APPLICATION_JSON)
	public void insertSpecificDeviceDiscreteStateJSON(
			@PathParam("device-id") String deviceId,
			@PathParam("state-name") String stateName,
			String jsonData);

}
