package meepo.writer.parquet;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.parquet.hadoop.ParquetFileWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.schema.MessageType;

import java.io.IOException;

public class ParquetWriterHelper extends ParquetWriter<Object[]> {

    @SuppressWarnings("deprecation") public ParquetWriterHelper(Path file, MessageType schema) throws IOException {
        super(file, new ParquetWriterSupport(schema), CompressionCodecName.SNAPPY, ParquetWriter.DEFAULT_BLOCK_SIZE / 4, ParquetWriter.DEFAULT_PAGE_SIZE);
    }

    @SuppressWarnings("deprecation") public ParquetWriterHelper(Path file, MessageType schema, String classpath) throws IOException {
        super(file, ParquetFileWriter.Mode.CREATE, new ParquetWriterSupport(schema), CompressionCodecName.SNAPPY, ParquetWriter.DEFAULT_BLOCK_SIZE / 4,
                ParquetWriter.DEFAULT_PAGE_SIZE, ParquetWriter.DEFAULT_PAGE_SIZE, DEFAULT_IS_DICTIONARY_ENABLED, DEFAULT_IS_VALIDATING_ENABLED, DEFAULT_WRITER_VERSION,
                createConf(classpath));
    }

    public static Configuration createConf(String classpath) {
        Configuration conf = new Configuration();
        conf.addResource(new Path(classpath + "core-site.xml"));
        conf.addResource(new Path(classpath + "hdfs-site.xml"));
        return conf;
    }

}
