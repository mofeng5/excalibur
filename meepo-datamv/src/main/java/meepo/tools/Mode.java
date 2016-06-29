package meepo.tools;

import meepo.reader.DefaultMysqlReader;
import meepo.reader.SyncMysqlReader;
import meepo.writer.DefaultMysqlWriter;
import meepo.writer.LoadDataMysqlWriter;
import meepo.writer.NullWriter;
import meepo.writer.ReplaceMysqlWriter;
import meepo.writer.parquet.ParquetWriter;

public enum Mode {

	SIMPLEREADER(DefaultMysqlReader.class),
	SYNCREADER(SyncMysqlReader.class), 
	
	SIMPLEWRITER(DefaultMysqlWriter.class),
	REPLACEWRITER(ReplaceMysqlWriter.class),
	LOADDATAWRITER(LoadDataMysqlWriter.class),
	
	NULLWRITER(NullWriter.class),
	
	PARQUETWRITER(ParquetWriter.class);

	Class<?> clazz;

	Mode(Class<?> clazz) {
		this.clazz = clazz;
	}
}
