package son.nt.dota2.comments;

import java.util.List;

/**
 * Created by sonnt on 11/7/16.
 */

public class CmtsHisotyContract {
    public interface View  {


        void showList(List<FullCmtsDto> fullCmtsDtos);
    }

    public interface Presenter {


        void getCmts();
    }
}
