using System.Web;

namespace Example.Utils
{
    public static class SessionManager
    {
        public static string SessionID
        {
            get
            {
                return (string)HttpContext.Current.Session["SessionID"];
            }
            set
            {
                HttpContext.Current.Session["SessionID"] = value;
            }
        }

        public static PollingBackgroundProcess PollingBackgroundProcess
        {
            get
            {
                return (PollingBackgroundProcess)HttpContext.Current.Session["PollingBackgroundProcess"];
            }
            set
            {
                HttpContext.Current.Session["PollingBackgroundProcess"] = value;
            }
        }

        public static AsyncSessionManager AsyncSessionManager
        {
            get
            {
                return (AsyncSessionManager)HttpContext.Current.Session["AsyncSessionManager"];
            }
            set
            {
                HttpContext.Current.Session["AsyncSessionManager"] = value;
            }
        }
    }
}
