package ServerClientShared;

/**
 *
 * @author Connor
 */
public enum FieldType {
    STRING{
        @Override
        public FieldWithoutContent getField(String fieldName, boolean required)
        {
            return new StringFieldWithoutContent(fieldName, required);
        }
    },
    
    IMAGE{
        @Override
        public FieldWithoutContent getField(String fieldName, boolean required)
        {
            return new ImageFieldWithoutContent(fieldName, required);
        }
    },
    
    VIDEO{
        @Override
        public FieldWithoutContent getField(String fieldName, boolean required)
        {
            return new VideoFieldWithoutContent(fieldName, required);
        }
    },
    
    AUDIO{
        @Override
        public FieldWithoutContent getField(String fieldName, boolean required)
        {
            return new AudioFieldWithoutContent(fieldName, required);
        }
    },
    
    CHECKBOX{
        @Override
        public FieldWithoutContent getField(String fieldName, boolean required)
        {
            throw new UnsupportedOperationException("Not implemented");
        }
    };
    
    public abstract FieldWithoutContent getField(String fieldName, boolean required);
}
