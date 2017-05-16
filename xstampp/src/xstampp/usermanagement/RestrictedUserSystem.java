package xstampp.usermanagement;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserSystem;

public class RestrictedUserSystem implements IUserSystem {

  @Override
  public boolean createUser() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean canCreateUser() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean deleteUser(UUID userId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean canDeleteUser(UUID userId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean grantAccessTo(IUser user, AccessRights right) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public UUID getSystemId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public UUID getCurrentUserId() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<IUser> getRegistry() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean checkAccess(UUID entryId, AccessRights accessRight) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IUser getCurrentUser() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean assignResponsibility(IUser user, UUID responsibility) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean assignResponsibilities(Map<UUID, IUser> responsibilityMap) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean createAdmin() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getSystemName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<UUID> getResponsibilities(UUID userId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isResponsible(UUID userId, UUID entryId) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isResponsible(UUID entryId) {
    // TODO Auto-generated method stub
    return false;
  }

}