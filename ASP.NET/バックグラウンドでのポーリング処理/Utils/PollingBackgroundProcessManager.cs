using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Example.Utils
{
    public static class PollingBackgroundProcessManager
    {
        private const int POLLING_TIME = 1000;

        private static List<PollingBackgroundProcess> pollingBackgroundProcessList = new List<PollingBackgroundProcess>();

        public static void Add(PollingBackgroundProcess process)
        {
            pollingBackgroundProcessList.Add(process);
        }

        async public static void Loop()
        {
            while(true)
            {
                pollingBackgroundProcessList = pollingBackgroundProcessList.Where((item) => item.IsLoop).ToList();
                await Task.WhenAll(
                    pollingBackgroundProcessList.Select((item) => item.call())
                );

                await Task.Delay(POLLING_TIME);
            }
        }
    }
}
