package kov.develop.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import kov.develop.client.GwtAppService;
import kov.develop.server.repository.PointRepository;
import kov.develop.shared.PointResult;
import kov.develop.shared.PointType;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class GwtAppServiceImpl extends RemoteServiceServlet implements GwtAppService {

    private PointRepository repository;
    private static long lastModified;


    public GwtAppServiceImpl() {
        this.repository = new PointRepository();
        PointRepository.readAllFromXml("E:\\Java\\Servicing\\src\\main\\resources\\point.xml");
        lastModified = new File("E:\\Java\\Servicing\\src\\main\\resources\\point.xml").lastModified();
        /*loader.setDaemon(true);
        loader.start();*/
    }


    public List<PointResult> getAllPoints() {
        return repository.getAllPoints().stream().map(p -> new PointResult(p)).collect(Collectors.toList());
    }

    public List<PointResult> getAllPointsByType(PointType type) {
        return repository.getAllPointsByType(type).stream().map(p -> new PointResult(p)).collect(Collectors.toList());
    }

    public List<PointResult> getAllPointsByTypeAndCountry(String type, String country) {
        return repository.getAllPointsByTypeAndCountry(type.equals("")? null : PointType.valueOf(type), country).stream().map(p -> new PointResult(p)).collect(Collectors.toList());
    }

    public List<PointResult> getAllPointsByTypeAndCountryAndSity(String type, String country, String sity) {
        return repository.getAllPointsByTypeAndCountryAndSity(type.equals("")? null : PointType.valueOf(type), country, sity).stream().map(p -> new PointResult(p)).collect(Collectors.toList());
    }

    public PointResult getPoint(int id) {
        return new PointResult(repository.getPoint(id));
    }

/*    Thread loader = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                if (new File("classpath:point.xml").lastModified() > lastModified) {
                    PointRepository.readAllFromXml("classpath:point.xml");
                    lastModified = new File("classpath:point.xml").lastModified();
                }
                System.out.println(new File("classpath:point.xml").lastModified());
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });*/
}
