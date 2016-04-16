package com.asksunny.schema.sample;

import java.sql.Types;

import com.asksunny.codegen.FieldDomainType;
import com.asksunny.schema.Entity;
import com.asksunny.schema.Field;
import com.asksunny.schema.Schema;

public class SampleSchemaCreator {

	public static Schema newSampleSchema() {
		Schema sample = new Schema("sample");

		Entity account = new Entity("Account");
		account.addField(Field.newField(Types.INTEGER, 0, 16, 16, false, "id", FieldDomainType.SEQUENCE, "1", null, null,
				null));

		account.addField(Field.newField(Types.VARCHAR, 0, 64, 64, false, "fname", FieldDomainType.FIRST_NAME, "1", null,
				null, null));
		account.addField(Field.newField(Types.VARCHAR, 0, 64, 64, false, "lname", FieldDomainType.LAST_NAME, "1", null,
				null, null));
		account.addField(Field.newField(Types.DATE, 0, 16, 16, false, "DOB", FieldDomainType.DATE, "1900-01-01",
				"2015-12-31", "yyyy-MM-dd", null));
		account.addField(Field.newField(Types.INTEGER, 0, 16, 16, false, "house_number", FieldDomainType.UINT, "1", "1000",
				null, null));
		account.addField(Field.newField(Types.VARCHAR, 0, 128, 128, false, "street", FieldDomainType.STREET, "1", "1000",
				null, null));

		account.addField(Field.newField(Types.VARCHAR, 0, 64, 64, false, "city", FieldDomainType.CITY, "1", "1000", null,
				null));

		account.addField(Field.newField(Types.VARCHAR, 0, 2, 2, false, "state", FieldDomainType.STATE, "1", "1000", null,
				null));

		account.addField(Field.newField(Types.VARCHAR, 0, 10, 10, false, "zip", FieldDomainType.ZIP, "1", "1000", null,
				null));

		account.addField(Field.newField(Types.VARCHAR, 0, 11, 11, false, "SSN", FieldDomainType.SSN, "1", "1000", null,
				null));

		account.addField(Field.newField(Types.VARCHAR, 0, 11, 11, false, "SACCNT", FieldDomainType.FORMATTED_STRING, "1",
				"1000", "XDX-DDD-DD-DDDD", null));

		Entity order = new Entity("Order");
		order.addField(Field.newField(Types.INTEGER, 0, 16, 16, false, "order_id", FieldDomainType.SEQUENCE, "1", null,
				null, null));
		Field field = Field.newField(Types.INTEGER, 0, 16, 16, false, "account_id", FieldDomainType.REF, null, null, null,
				null);
		field.setReference(account.getFields().get(0));
		order.addField(field);
		order.addField(Field.newField(Types.TIMESTAMP, 0, 16, 16, false, "created_date", FieldDomainType.DATE,
				"2000-01-01 00:00:00", "2015-12-31 23:59:59", "yyyy-MM-dd HH:mm:ss", null));
		order.addField(Field.newField(Types.DOUBLE, 2, 10, 12, false, "total_price", FieldDomainType.UDOUBLE, "1", null,
				"yyyy-MM-dd HH:mm:ss", null));
		sample.put(account.getName(), account);
		sample.put(order.getName(), order);

		return sample;

	}

	public static void main(String[] args) {

	}

}
