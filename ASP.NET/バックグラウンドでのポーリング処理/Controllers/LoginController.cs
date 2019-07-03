using System.Threading.Tasks;
using System.Web.Mvc;
using Example.Models;
using Example.Utils;

namespace Example.Controllers
{
    public class LoginController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        [HttpPost]
        public ActionResult Login(LoginModel model)
        {
            // ログイン成功したことにする

            // ログイン成功したユーザーのポーリング処理を登録する
            SessionManager.AsyncSessionManager = new AsyncSessionManager(System.Web.HttpContext.Current.Session);
            SessionManager.AsyncSessionManager.test = 0;
            SessionManager.PollingBackgroundProcess = new PollingBackgroundProcess(SessionManager.AsyncSessionManager);
            PollingBackgroundProcessManager.Add(SessionManager.PollingBackgroundProcess);

            return View("~/Views/Test/Index.cshtml", model);
        }

        public ActionResult Logout()
        {
            SessionManager.PollingBackgroundProcess.stop();
            return View("~/Views/Login/Index.cshtml");
        }
    }
}
