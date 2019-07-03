using System.Threading.Tasks;

namespace Example.Utils
{
    /// <summary>
    /// ユーザーが正常な手段でログアウトしなかった場合対応できない
    /// </summary>
    public class PollingBackgroundProcess
    {
        public bool IsLoop = true;

        private const int pollingTime = 1000;
        private AsyncSessionManager asyncSessionManager = null;

        public PollingBackgroundProcess(AsyncSessionManager asyncSessionManager)
        {
            this.asyncSessionManager = asyncSessionManager;
        }

        async public Task call()
        {
            if(IsLoop)
            {
                asyncSessionManager.test = asyncSessionManager.test + 1;

                await Task.WhenAll(
                    AsyncTest(),
                    AsyncTest()
                ).ConfigureAwait(false);
            }

            // 条件によりループを終了させる必要がある
        }

        public void stop()
        {
            IsLoop = false;
        }

        async private Task AsyncTest()
        {
            await Task.Delay(1);
        }
    }
}
