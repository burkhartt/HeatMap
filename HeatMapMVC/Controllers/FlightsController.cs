using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Web.Mvc;

namespace HeatMapMVC.Controllers {
	public class FlightsController : Controller {		
		private int GetRandomTravelers() {
			var rand = new Random();
			return rand.Next(2835, 583285);
		}		

		private IEnumerable<Airport> GetAirports() {
			var lines = System.IO.File.ReadAllLines(@"c:\_Application\HeatMapMVC\HeatMapMVC\airports.dat");
			var airports = new List<Airport>();
			foreach (var line in lines) {
				try {
					var data = line.Split(',');
					airports.Add(new Airport {
						Latitude = Convert.ToDouble(data[6]),
						Longitude = Convert.ToDouble(data[7])
					});
				} catch (Exception) { }
			}
			return airports;
		}

		public ActionResult Index() {
			var airports = GetAirports();
			var flightDatas = new List<dynamic>();

			foreach (var airport in airports) {
				flightDatas.Add(new {
					NumberOfTravelers = GetRandomTravelers(),
					DestinationLat = airport.Latitude,
					DestinationLong = airport.Longitude,
					//Date = new DateTime(2013, 01, 01)
				});
			}

			return Json(flightDatas, JsonRequestBehavior.AllowGet);
			//return Json(new {NumberOfTravelers = 35, DestinationLat = -32, DestinationLong = 58}, JsonRequestBehavior.AllowGet);
		}
	}

	internal class Airport {
		public double Latitude { get; set; }
		public double Longitude { get; set; }
	}

	public class FlightData {
		public int NumberOfTravelers { get; set; }
		public double DestinationLat { get; set; }
		public double DestinationLong { get; set; }
		public DateTime Date { get; set; }
	}
}