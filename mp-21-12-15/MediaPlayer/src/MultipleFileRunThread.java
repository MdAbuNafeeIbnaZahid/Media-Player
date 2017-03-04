/**
 * Created by nafeedgbhs on 12/9/2015.
 */


public class MultipleFileRunThread implements Runnable
{
    Thread thr;
    Main main;
    MediaPlayerController mediaPlayerController;

    MultipleFileRunThread(Main main, MediaPlayerController mediaPlayerController)
    {
        this.main = main;
        this.mediaPlayerController = mediaPlayerController;
        mediaPlayerController.previous = 0;
        mediaPlayerController.next = 0;
        thr = new Thread(this);
        thr.start();
    }

    @Override
    public void run() {
        for ( int i = 0; i<main.playList.size() ; i++ )
        {
            System.out.println(main.playList.get(i));
            mediaPlayerController.currentFile = main.playList.get(i);

            try {
                mediaPlayerController.makeMediaAndPlay(main.playList.get(i).getPath());
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
            }



            try
            {
                //wait till a media playing is not completed
                while (  ! mediaPlayerController.getMediaPlayer().getTotalDuration().equals(mediaPlayerController.getMediaPlayer().getCurrentTime() )  )
                {

                    // i needs to updated always, because i might be changed because of randomized
                    i = main.playList.indexOf( mediaPlayerController.currentFile );



                    if (mediaPlayerController.previous == 1)
                    {
                        //
                        mediaPlayerController.previous = 0;

                        if (i <= 0)
                        {
                            System.out.println("It's the first item!!!!! \n Previous not found");
                        }

                        else
                        {
                            System.out.println("Previous will be played");
                            i -= 2;
                            break;
                        }
                    }

                    if (mediaPlayerController.next == 1)
                    {

                        mediaPlayerController.next = 0;


                        if (i >= main.playList.size()-1)
                        {
                            System.out.println("It's the last item!!!!! \n Next not found");
                        }

                        else
                        {
                            System.out.println("Next will be played");
                            //i += 1;
                            break;
                        }
                    }

                }


                if (   mediaPlayerController.getMediaPlayer().getTotalDuration().equals(mediaPlayerController.getMediaPlayer().getCurrentTime() )
                        && mediaPlayerController.isLoop == 1 )
                {
                    i = i-1;
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getStackTrace());
            }

        }
    }
}

