#sourceMode=SIMPLEREADER,SYNCREADER
sourceTableName=test_1
#primaryKeyName=[AUTO]
#sourceColumnsNames=[AUTO] | id,col1,col2,col3
#sourceExtraSQL=
#readerStepSize=100
#readersNum=1
#start=[AUTO]
#end=[AUTO]
#endDelay=0

#targetMode=SIMPLEWRITER,REPLACEWRITER,LOADDATAWRITER,NULLWRITER
targetTableName=test_2
#targetColumnsNames=[AUTO] | id,col1,col2,col3
#writerStepSize=100
#writersNum=1

#bufferSize=8192
#pluginClass=meepo.storage.plugin.ParseColumnTypePlugin
