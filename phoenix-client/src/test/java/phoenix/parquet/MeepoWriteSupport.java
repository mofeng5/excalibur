package phoenix.parquet;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.column.ColumnDescriptor;
import org.apache.parquet.hadoop.api.WriteSupport;
import org.apache.parquet.io.ParquetEncodingException;
import org.apache.parquet.io.api.Binary;
import org.apache.parquet.io.api.RecordConsumer;
import org.apache.parquet.schema.MessageType;

import com.google.common.collect.Maps;

public class MeepoWriteSupport extends WriteSupport<String[]> {

	private RecordConsumer recordConsumer;

	private MessageType schema;

	private List<ColumnDescriptor> columns;

	public MeepoWriteSupport(MessageType schema) {
		this.schema = schema;
		this.columns = schema.getColumns();
	}

	@Override
	public WriteContext init(Configuration configuration) {
		return new WriteContext(schema, Maps.newHashMap());
	}

	@Override
	public void prepareForWrite(RecordConsumer recordConsumer) {
		this.recordConsumer = recordConsumer;
	}

	@Override
	public void write(String[] record) {
		if (record.length != this.columns.size()) {
			throw new ParquetEncodingException("Invalid input data. Expecting " + this.columns.size()
					+ " columns. Input had " + record.length + " columns (" + this.columns + ") : " + record);
		}
		this.recordConsumer.startMessage();
		for (int i = 0; i < this.columns.size(); i++) {
			String val = record[i];
			if (val != null) {
				this.recordConsumer.startField(columns.get(i).getPath()[0], i);
				switch (columns.get(i).getType()) {
				case BOOLEAN:
					this.recordConsumer.addBoolean(Boolean.valueOf(val));
					break;
				case FLOAT:
					this.recordConsumer.addFloat(Float.valueOf(val));
					break;
				case DOUBLE:
					this.recordConsumer.addDouble(Double.valueOf(val));
					break;
				case INT32:
					this.recordConsumer.addInteger(Integer.valueOf(val));
					break;
				case INT64:
					this.recordConsumer.addLong((Long.valueOf(val)));
					break;
				case BINARY:
					this.recordConsumer.addBinary(Binary.fromString(val));
					break;
				default:
					throw new ParquetEncodingException("Unsupported column type: " + this.columns.get(i).getType());
				}
				this.recordConsumer.endField(this.columns.get(i).getPath()[0], i);
			}
		}
		recordConsumer.endMessage();
	}

}
