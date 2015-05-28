package org.shm.monitoring.service;

import org.shm.monitoring.domain.ProjectConfiguration;
import org.shm.monitoring.repository.ProjectConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigurationServices {

    @Autowired
    private ProjectConfigurationRepository configurationDao;



    public boolean replaceConfiguration(List<ProjectConfiguration> projectConfigurations) {


        //int nb=
        configurationDao.deleteAll();
        //System.out.println(nb);


        for (ProjectConfiguration projectConfiguration : projectConfigurations) {
            configurationDao.save(projectConfiguration);

        }

        return true;

    }

    public boolean createOrUpdateConfigration(ProjectConfiguration projectConfiguration) {

        if (projectConfiguration.getId() == null) {
            configurationDao.save(projectConfiguration);
        } else {
            /*
             * on recopie les champs pour �viter d'ecraser les stat
			 * qui ont �t� d�normalis� dans la table configuration
			 */

            ProjectConfiguration old = configurationDao.findOne(projectConfiguration.getId());
            old.setActif(projectConfiguration.getActif());
            old.setAlertMail(projectConfiguration.getAlertMail());
            old.setAlertSMS(projectConfiguration.getAlertSMS());

            old.setCheckMessage(projectConfiguration.getCheckMessage());
            old.setEmail(projectConfiguration.getEmail());
            old.setFrequence(projectConfiguration.getFrequence());
            old.setLogin(projectConfiguration.getLogin());
            old.setName(projectConfiguration.getName());
            old.setParametre(projectConfiguration.getParametre());
            old.setPassword2(projectConfiguration.getPassword2());
            old.setPost(projectConfiguration.getPost());
            old.setRequestMethod(projectConfiguration.getRequestMethod());
            old.setUrl(projectConfiguration.getUrl());
            old.setHeader(projectConfiguration.getHeader());
            old.setSoap(projectConfiguration.getSoap());
            old.setContentType(projectConfiguration.getContentType());

            configurationDao.save(old);

        }

        return true;
    }

}
