package ServerClientShared;

import java.util.ArrayList;

/**
 * Created by wsv759 on 18/02/15.
 * This enumerates the types of objects (or files) that could constitute
 * the content for a field.
 */
public enum FieldType {
    IMAGE{

        @Override
        public void populateUI(UITYPE UI) {
            UI.addImage();
        }
        
    },
    STRING{

        @Override
        public void populateUI(UITYPE UI) {
            UI.addString();
        }
        
    },
    VIDEO{

        @Override
        public void populateUI(UITYPE UI) {
            UI.addVideo();
        }
        
    },
    AUDIO{

        @Override
        public void populateUI(UITYPE UI) {
            UI.addAudio();
        }
        
    },
    OPTION_LIST{
        
        public ArrayList<String> options;        
        
        @Override
        public void populateUI(UITYPE UI) {
            UI.addOptions(options);
        }
    }
    ; // <-- end of type/implementation definitions
    
    public abstract void populateUI(UITYPE UI);
}
