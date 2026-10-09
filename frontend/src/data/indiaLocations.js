// India states / union territories with their major cities.
// Used to power the state + city branch location filters and the branch admin form.
export const INDIA_STATES = [
  {
    name: "Andaman and Nicobar Islands",
    cities: ["Port Blair"],
  },
  {
    name: "Andhra Pradesh",
    cities: ["Visakhapatnam", "Vijayawada", "Guntur", "Nellore", "Tirupati", "Kurnool", "Rajahmundry", "Kakinada"],
  },
  {
    name: "Arunachal Pradesh",
    cities: ["Itanagar", "Naharlagun", "Pasighat"],
  },
  {
    name: "Assam",
    cities: ["Guwahati", "Dibrugarh", "Silchar", "Jorhat", "Nagaon", "Tinsukia"],
  },
  {
    name: "Bihar",
    cities: ["Patna", "Gaya", "Bhagalpur", "Muzaffarpur", "Darbhanga", "Purnia"],
  },
  {
    name: "Chandigarh",
    cities: ["Chandigarh"],
  },
  {
    name: "Chhattisgarh",
    cities: ["Raipur", "Bhilai", "Bilaspur", "Korba", "Durg"],
  },
  {
    name: "Dadra and Nagar Haveli and Daman and Diu",
    cities: ["Silvassa", "Daman", "Diu"],
  },
  {
    name: "Delhi",
    cities: ["New Delhi", "Dwarka", "Rohini", "Saket", "Karol Bagh", "Pitampura", "Janakpuri"],
  },
  {
    name: "Goa",
    cities: ["Panaji", "Margao", "Vasco da Gama", "Mapusa", "Ponda"],
  },
  {
    name: "Gujarat",
    cities: ["Ahmedabad", "Surat", "Vadodara", "Rajkot", "Gandhinagar", "Bhavnagar", "Jamnagar", "Anand"],
  },
  {
    name: "Haryana",
    cities: ["Gurugram", "Faridabad", "Panipat", "Ambala", "Karnal", "Hisar", "Rohtak"],
  },
  {
    name: "Himachal Pradesh",
    cities: ["Shimla", "Solan", "Dharamshala", "Mandi", "Baddi"],
  },
  {
    name: "Jammu and Kashmir",
    cities: ["Srinagar", "Jammu", "Anantnag", "Baramulla", "Udhampur"],
  },
  {
    name: "Jharkhand",
    cities: ["Ranchi", "Jamshedpur", "Dhanbad", "Bokaro", "Deoghar"],
  },
  {
    name: "Karnataka",
    cities: ["Bengaluru", "Mysuru", "Mangaluru", "Hubballi", "Belagavi", "Davangere", "Shivamogga"],
  },
  {
    name: "Kerala",
    cities: ["Thiruvananthapuram", "Kochi", "Kozhikode", "Thrissur", "Kollam", "Kannur", "Alappuzha"],
  },
  {
    name: "Ladakh",
    cities: ["Leh", "Kargil"],
  },
  {
    name: "Lakshadweep",
    cities: ["Kavaratti"],
  },
  {
    name: "Madhya Pradesh",
    cities: ["Indore", "Bhopal", "Gwalior", "Jabalpur", "Ujjain", "Rewa", "Sagar"],
  },
  {
    name: "Maharashtra",
    cities: ["Mumbai", "Pune", "Nagpur", "Thane", "Nashik", "Aurangabad", "Navi Mumbai", "Kolhapur", "Solapur"],
  },
  {
    name: "Manipur",
    cities: ["Imphal", "Thoubal"],
  },
  {
    name: "Meghalaya",
    cities: ["Shillong", "Tura"],
  },
  {
    name: "Mizoram",
    cities: ["Aizawl", "Lunglei"],
  },
  {
    name: "Nagaland",
    cities: ["Kohima", "Dimapur"],
  },
  {
    name: "Odisha",
    cities: ["Bhubaneswar", "Cuttack", "Rourkela", "Puri", "Sambalpur"],
  },
  {
    name: "Puducherry",
    cities: ["Puducherry", "Karaikal"],
  },
  {
    name: "Punjab",
    cities: ["Ludhiana", "Amritsar", "Jalandhar", "Mohali", "Patiala", "Bathinda"],
  },
  {
    name: "Rajasthan",
    cities: ["Jaipur", "Jodhpur", "Udaipur", "Kota", "Ajmer", "Bikaner", "Alwar"],
  },
  {
    name: "Sikkim",
    cities: ["Gangtok", "Namchi"],
  },
  {
    name: "Tamil Nadu",
    cities: ["Chennai", "Coimbatore", "Madurai", "Tiruchirappalli", "Salem", "Tirunelveli", "Vellore", "Erode"],
  },
  {
    name: "Telangana",
    cities: ["Hyderabad", "Secunderabad", "Warangal", "Karimnagar", "Nizamabad"],
  },
  {
    name: "Tripura",
    cities: ["Agartala", "Ambassa", "Kailashahar"],
  },
  {
    name: "Uttar Pradesh",
    cities: ["Lucknow", "Noida", "Ghaziabad", "Kanpur", "Varanasi", "Agra", "Meerut", "Prayagraj", "Bareilly"],
  },
  {
    name: "Uttarakhand",
    cities: ["Dehradun", "Haridwar", "Roorkee", "Haldwani", "Rishikesh"],
  },
  {
    name: "West Bengal",
    cities: ["Kolkata", "Howrah", "Siliguri", "Durgapur", "Asansol", "Kharagpur"],
  },
];

export const STATE_NAMES = INDIA_STATES.map((state) => state.name);

export const getCitiesForState = (stateName) => {
  const match = INDIA_STATES.find((state) => state.name === stateName);
  return match ? match.cities : [];
};
