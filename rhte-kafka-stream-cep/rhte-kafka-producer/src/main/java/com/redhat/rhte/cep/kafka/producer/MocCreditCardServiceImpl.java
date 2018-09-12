package com.redhat.rhte.cep.kafka.producer;

import java.util.Date;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.redhat.rhte.cep.kafka.model.CreditCardTransaction;

@Service("mocCreditCardService")
public class MocCreditCardServiceImpl implements MocCreditCardService {

	static Integer count = 1;
	static String[]  countries = new String[] { "Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola",
			"Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",
			"Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin",
			"Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil",
			"British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia",
			"Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China",
			"Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo",
			"Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire",
			"Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica",
			"Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea",
			"Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France",
			"France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon",
			"Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe",
			"Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands",
			"Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia",
			"Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan",
			"Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait",
			"Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia",
			"Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau",
			"Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali",
			"Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico",
			"Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco",
			"Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles",
			"New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island",
			"Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay",
			"Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania",
			"Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines",
			"Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone",
			"Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa",
			"South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena",
			"St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden",
			"Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan",
			"Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia",
			"Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates",
			"United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan",
			"Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)",
			"Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe" };
	
//	static String[] creditCards = new String[10000];
	static String[] creditCards = new String[2];
	{
		//for(int i=0;i<10000;i++)
		for(int i=0;i<2;i++)
		{
			Random rand = new Random();
			int creditRandom = rand.nextInt(99999999);
			String creditCardId = "54091725" + String.format("%08d", creditRandom);
			creditCards[i]=creditCardId;
		}
	}

	@Override
	public String generateMessage() {

		String message = "Moc messages \"Hello world - {" + ++count + "}";
		return message;

	}

	@Override
	public CreditCardTransaction generateCreditCardTransaction() {
		Random rand = new Random();
		String creditCardId = getRandomCreditCard();
		String itemPurchased = "Item Purchased for - " + creditCardId;
		double amount = rand.nextDouble() * 1000;
		Date purchaseDate = new Date(System.currentTimeMillis());
		String country = getRandomCountry();
		String zipCode = "zipCode for - " + creditCardId;
		String storeId = "" + rand.nextInt(10000);

		CreditCardTransaction dummyTrans = new CreditCardTransaction(creditCardId, itemPurchased, amount, purchaseDate,
				country, zipCode, storeId);
		return dummyTrans;
	}

	private String getRandomCountry() {
		int rnd = new Random().nextInt(countries.length);
		return countries[rnd];
	}
	
	private String getRandomCreditCard() {
		int rnd = new Random().nextInt(creditCards.length);
		return creditCards[rnd];
	}
}