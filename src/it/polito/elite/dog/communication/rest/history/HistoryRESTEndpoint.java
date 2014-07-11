/**
 * 
 */
package it.polito.elite.dog.communication.rest.history;

import it.polito.elite.dog.addons.storage.EventDataStreamSet;
import it.polito.elite.dog.addons.storage.EventStore;
import it.polito.elite.dog.addons.storage.EventStoreInfo;
import it.polito.elite.dog.communication.rest.history.api.HistoryRESTApi;
import it.polito.elite.dog.core.library.util.LogHelper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

/**
 * @author bonino
 * 
 */
@Path("/api/v1/history/")
public class HistoryRESTEndpoint implements HistoryRESTApi
{
	// the service logger
	private LogHelper logger;

	// the bundle context reference to extract information on the entire Dog
	// status
	private BundleContext context;

	// the atomic reference to the EventStore service
	private AtomicReference<EventStore> eventStore;

	// the instance-level mapper
	private ObjectMapper mapper;

	/**
	 * Constructor
	 */
	public HistoryRESTEndpoint()
	{
		// initialize the atomic reference
		this.eventStore = new AtomicReference<>();

		// initialize the instance-wide object mapper
		this.mapper = new ObjectMapper();
		// set the mapper pretty printing
		this.mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
		// avoid empty arrays and null values
		this.mapper.configure(
				SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
		this.mapper.setSerializationInclusion(Inclusion.NON_NULL);

		// create an introspector for parsing Jackson annotations
		AnnotationIntrospector jackson = new JacksonAnnotationIntrospector();

		// make deserializer use Jackson
		this.mapper.getDeserializationConfig().withAnnotationIntrospector(
				jackson);
		// make serializer use Jackson
		this.mapper.getSerializationConfig()
				.withAnnotationIntrospector(jackson);

	}

	/**
	 * Bundle activation, stores a reference to the context object passed by the
	 * framework to get access to system data, e.g., installed bundles, etc.
	 * 
	 * @param context
	 */
	public void activate(BundleContext context)
	{
		// store the bundle context
		this.context = context;

		// init the logger with a null logger
		this.logger = new LogHelper(this.context);

		// log the activation
		this.logger.log(LogService.LOG_INFO, "Activated....");
	}

	/**
	 * Prepare the bundle to be deactivated...
	 */
	public void deactivate()
	{
		// null the context
		this.context = null;

		// log deactivation
		this.logger.log(LogService.LOG_INFO, "Deactivated...");

		// null the logger
		this.logger = null;
	}

	/**
	 * Binds the event store service needed for extracting / putting required
	 * historical data.
	 * 
	 * @param store
	 */
	public void addedEventStore(EventStore store)
	{
		// store the reference to the event store
		this.eventStore.set(store);
	}

	/**
	 * Removes the reference to the given event store
	 * 
	 * @param store
	 */
	public void removedEventStore(EventStore store)
	{
		// remove the reference to the given event store
		this.eventStore.compareAndSet(store, null);
	}

	@Override
	public String getAllDeviceParametricNotifications(String deviceId,
			String startDate, String endDate, Integer offset, Integer limit)
	{
		return this.getEventData(deviceId, startDate, endDate, offset, limit,
				null, EventDataType.MEASURE, EventType.NOTIFICATION);
	}

	@Override
	public String getAllDeviceNonParametricNotifications(String deviceId,
			String startDate, String endDate, Integer offset, Integer limit,
			Boolean aggregate)
	{
		return this.getEventData(deviceId, startDate, endDate,
				offset, limit, aggregate, EventDataType.NOTMEASURE, EventType.NOTIFICATION);
	}

	
	@Override
	public String getAllDeviceContinuousStates(String deviceId,
			String startDate, String endDate, Integer offset, Integer limit)
	{
		return this.getEventData(deviceId, startDate, endDate, offset, limit,
				null, EventDataType.MEASURE, EventType.STATE);
	}

	@Override
	public String getAllDeviceDiscreteStates(String deviceId, String startDate,
			String endDate, Integer offset, Integer limit, Boolean aggregate)
	{
		return this.getEventData(deviceId, startDate, endDate,
				offset, limit, aggregate, EventDataType.NOTMEASURE, EventType.STATE);
	}
	
	
	private String getEventData(String deviceId, String startDate,
			String endDate, Integer offset, Integer limit, Boolean aggregate,
			EventDataType dataType, EventType type)
	{
		// The extracted notifications as JSON
		String extractedNotificationsJSON = "";

		// prepare the parameters
		SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSSXX");

		// parse the dates if present
		Date start = new Date(0); // default, starts from the epoch
		if ((startDate != null) && (!startDate.isEmpty()))
		{
			try
			{
				start = sdf.parse(startDate);
			}
			catch (ParseException e)
			{
				this.logger.log(LogService.LOG_ERROR,
						"Unable to parse the start date");
				throw new WebApplicationException(Response.Status.BAD_REQUEST);
			}
		}

		// parse the dates if present
		Date end = new Date(); // default, the current time
		if ((endDate != null) && (!endDate.isEmpty()))
		{
			try
			{
				end = sdf.parse(endDate);
			}
			catch (ParseException e)
			{
				this.logger.log(LogService.LOG_ERROR,
						"Unable to parse the end date");
				throw new WebApplicationException(Response.Status.BAD_REQUEST);
			}
		}

		// if the offset is not specified set the default value of 0 , i.e.,
		// start from the first result.
		if (offset == null)
			offset = 0;

		// if the limit parameter is not specified, set the default value at
		// EventStoreInfo.UNLIMITED_SIZE, i.e., do not limit result size.
		if (limit == null)
			limit = EventStoreInfo.UNLIMITED_SIZE;

		// if the aggregate parameter is not specified, set the default at true,
		// i.e., results will be aggregated in a single stream.
		if (aggregate == null)
		{
			aggregate = true;
		}

		try
		{
			EventDataStreamSet events = new EventDataStreamSet();

			switch (type)
			{
				case NOTIFICATION:
				{
					switch (dataType)
					{
						case MEASURE:
						{
							events = this.eventStore
									.get()
									.getAllDeviceParametricNotifications(
											deviceId, start, end, offset, limit);
							break;
						}
						case NOTMEASURE:
						{
							events = this.eventStore.get()
									.getAllDeviceNonParametricNotifications(
											deviceId, start, end, offset,
											limit, aggregate);
							break;
						}
					}

					break;
				}
				case STATE:
				{
					switch (dataType)
					{
						case MEASURE:
						{
							events = this.eventStore.get()
									.getAllDeviceContinuousStates(deviceId,
											start, end, offset, limit);
							break;
						}
						case NOTMEASURE:
						{
							events = this.eventStore.get()
									.getAllDeviceDiscreteStates(deviceId,
											start, end, offset, limit,
											aggregate);
							break;
						}
					}
				}
			}

			extractedNotificationsJSON = this.mapper.writeValueAsString(events);
		}
		catch (IOException e)
		{
			this.logger.log(LogService.LOG_ERROR,
					"Unable to compose the response message for the paramteric notifications of "
							+ deviceId, e);
		}

		if (extractedNotificationsJSON.isEmpty())
		{
			// launch the exception responsible for sending the HTTP response
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		else
			return extractedNotificationsJSON;
	}
}
