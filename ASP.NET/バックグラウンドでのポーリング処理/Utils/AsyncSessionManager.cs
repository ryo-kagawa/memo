using System.Web.SessionState;

namespace Example.Utils
{
    public class AsyncSessionManager
    {
        private HttpSessionState Session { get; set; }

        public AsyncSessionManager(HttpSessionState session)
        {
            Session = session;
        }

        public int test
        {
            get
            {
                return (int)Session["test"];
            }
            set
            {
                Session["test"] = value;
            }
        }
    }
}
