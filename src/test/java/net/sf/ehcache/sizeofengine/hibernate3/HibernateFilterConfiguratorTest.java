package net.sf.ehcache.sizeofengine.hibernate3;

import net.sf.ehcache.pool.SizeOfEngine;
import net.sf.ehcache.pool.SizeOfEngineLoader;
import net.sf.ehcache.pool.impl.DefaultSizeOfEngine;
import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.ReplicationMode;
import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.TypeHelper;
import org.hibernate.UnknownProfileException;
import org.hibernate.collection.PersistentCollection;
import org.hibernate.engine.EntityKey;
import org.hibernate.engine.LoadQueryInfluencers;
import org.hibernate.engine.NonFlushedChanges;
import org.hibernate.engine.PersistenceContext;
import org.hibernate.engine.QueryParameters;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.engine.query.sql.NativeSQLQuerySpecification;
import org.hibernate.event.EventListeners;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.jdbc.Batcher;
import org.hibernate.jdbc.JDBCContext;
import org.hibernate.jdbc.Work;
import org.hibernate.loader.custom.CustomQuery;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.stat.SessionStatistics;
import org.hibernate.type.Type;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class HibernateFilterConfiguratorTest {

  private SizeOfEngine filteringSizeOfEngine;
  private SizeOfEngine defaultSizeOfEngine;

  @Before
  public void setup() {
    filteringSizeOfEngine = SizeOfEngineLoader.newSizeOfEngine(10, true, true);
    defaultSizeOfEngine = new DefaultSizeOfEngine(10, true, true);
  }

  @Test
  public void testLoadsAllFields() {
    assertThat(HibernateFilterConfigurator.ignoredFields.size(), is(7));
  }

  @Test
  public void testFiltersEntityModeClassInstances() {
    EntityModeObject entityModeObject = new EntityModeObject();
    final long withoutFiltering = defaultSizeOfEngine.sizeOf(entityModeObject, entityModeObject, entityModeObject).getCalculated();
    final long withFiltering = filteringSizeOfEngine.sizeOf(entityModeObject, entityModeObject, entityModeObject).getCalculated();
    assertThat(withFiltering < withoutFiltering, is(true));
  }

  @Test
  public void testFiltersSessionClassInstances() {
    SessionObject sessionObject = new SessionObject();
    final long withoutFiltering = defaultSizeOfEngine.sizeOf(sessionObject, sessionObject, sessionObject).getCalculated();
    final long withFiltering = filteringSizeOfEngine.sizeOf(sessionObject, sessionObject, sessionObject).getCalculated();
    assertThat(withFiltering < withoutFiltering, is(true));
  }

  @Test
  public void testFiltersSessionImplementorClassInstances() {
    SessionImplementorObject sessionImplementorObject = new SessionImplementorObject();
    final long withoutFiltering = defaultSizeOfEngine.sizeOf(sessionImplementorObject, sessionImplementorObject, sessionImplementorObject).getCalculated();
    final long withFiltering = filteringSizeOfEngine.sizeOf(sessionImplementorObject, sessionImplementorObject, sessionImplementorObject).getCalculated();
    assertThat(withFiltering < withoutFiltering, is(true));
  }

  public static class EntityModeObject {
    EntityMode mode = new EntityMode("FOO");
    int value;
  }
  public static class SessionObject {
    Session session = new Session() {

      @Override
      public EntityMode getEntityMode() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Session getSession(final EntityMode entityMode) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void flush() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setFlushMode(final FlushMode flushMode) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public FlushMode getFlushMode() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setCacheMode(final CacheMode cacheMode) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public CacheMode getCacheMode() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public SessionFactory getSessionFactory() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Connection connection() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Connection close() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void cancelQuery() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isOpen() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isConnected() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isDirty() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isDefaultReadOnly() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setDefaultReadOnly(final boolean b) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Serializable getIdentifier(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean contains(final Object o) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void evict(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object load(final Class aClass, final Serializable serializable, final LockMode lockMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object load(final Class aClass, final Serializable serializable, final LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object load(final String s, final Serializable serializable, final LockMode lockMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object load(final String s, final Serializable serializable, final LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object load(final Class aClass, final Serializable serializable) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object load(final String s, final Serializable serializable) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void load(final Object o, final Serializable serializable) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void replicate(final Object o, final ReplicationMode replicationMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void replicate(final String s, final Object o, final ReplicationMode replicationMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Serializable save(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Serializable save(final String s, final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void saveOrUpdate(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void saveOrUpdate(final String s, final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void update(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void update(final String s, final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object merge(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object merge(final String s, final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void persist(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void persist(final String s, final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void delete(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void delete(final String s, final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void lock(final Object o, final LockMode lockMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void lock(final String s, final Object o, final LockMode lockMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public LockRequest buildLockRequest(final LockOptions lockOptions) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void refresh(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void refresh(final Object o, final LockMode lockMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void refresh(final Object o, final LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public LockMode getCurrentLockMode(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Transaction beginTransaction() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Transaction getTransaction() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Criteria createCriteria(final Class aClass) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Criteria createCriteria(final Class aClass, final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Criteria createCriteria(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Criteria createCriteria(final String s, final String s2) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Query createQuery(final String s) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public SQLQuery createSQLQuery(final String s) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Query createFilter(final Object o, final String s) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Query getNamedQuery(final String s) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void clear() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object get(final Class aClass, final Serializable serializable) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object get(final Class aClass, final Serializable serializable, final LockMode lockMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object get(final Class aClass, final Serializable serializable, final LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object get(final String s, final Serializable serializable) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object get(final String s, final Serializable serializable, final LockMode lockMode) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object get(final String s, final Serializable serializable, final LockOptions lockOptions) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public String getEntityName(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Filter enableFilter(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Filter getEnabledFilter(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void disableFilter(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public SessionStatistics getStatistics() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isReadOnly(final Object o) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setReadOnly(final Object o, final boolean b) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void doWork(final Work work) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Connection disconnect() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void reconnect() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void reconnect(final Connection connection) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isFetchProfileEnabled(final String s) throws UnknownProfileException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void enableFetchProfile(final String s) throws UnknownProfileException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void disableFetchProfile(final String s) throws UnknownProfileException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public TypeHelper getTypeHelper() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public LobHelper getLobHelper() {
        throw new UnsupportedOperationException("Implement me!");
      }
    };
    int value;
  }
  public static class SessionImplementorObject {
    SessionImplementor sessionImplementor = new SessionImplementor() {

      @Override
      public Interceptor getInterceptor() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setAutoClear(final boolean b) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isTransactionInProgress() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void initializeCollection(final PersistentCollection persistentCollection, final boolean b) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object internalLoad(final String s, final Serializable serializable, final boolean b, final boolean b2) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object immediateLoad(final String s, final Serializable serializable) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public long getTimestamp() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public SessionFactoryImplementor getFactory() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Batcher getBatcher() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public List list(final String s, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Iterator iterate(final String s, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public ScrollableResults scroll(final String s, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public ScrollableResults scroll(final CriteriaImpl criteria, final ScrollMode scrollMode) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public List list(final CriteriaImpl criteria) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public List listFilter(final Object o, final String s, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Iterator iterateFilter(final Object o, final String s, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public EntityPersister getEntityPersister(final String s, final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object getEntityUsingInterceptor(final EntityKey entityKey) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void afterTransactionCompletion(final boolean b, final Transaction transaction) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void beforeTransactionCompletion(final Transaction transaction) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Serializable getContextEntityIdentifier(final Object o) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public String bestGuessEntityName(final Object o) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public String guessEntityName(final Object o) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object instantiate(final String s, final Serializable serializable) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public List listCustomQuery(final CustomQuery customQuery, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public ScrollableResults scrollCustomQuery(final CustomQuery customQuery, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public List list(final NativeSQLQuerySpecification nativeSQLQuerySpecification, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public ScrollableResults scroll(final NativeSQLQuerySpecification nativeSQLQuerySpecification, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Object getFilterParameterValue(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Type getFilterParameterType(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Map getEnabledFilters() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public int getDontFlushFromFind() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public EventListeners getListeners() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public PersistenceContext getPersistenceContext() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public int executeUpdate(final String s, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public int executeNativeUpdate(final NativeSQLQuerySpecification nativeSQLQuerySpecification, final QueryParameters queryParameters) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public NonFlushedChanges getNonFlushedChanges() throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void applyNonFlushedChanges(final NonFlushedChanges nonFlushedChanges) throws HibernateException {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public EntityMode getEntityMode() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public CacheMode getCacheMode() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setCacheMode(final CacheMode cacheMode) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isOpen() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isConnected() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public FlushMode getFlushMode() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setFlushMode(final FlushMode flushMode) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Connection connection() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void flush() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Query getNamedQuery(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public Query getNamedSQLQuery(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isEventSource() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void afterScrollOperation() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public String getFetchProfile() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public void setFetchProfile(final String s) {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public JDBCContext getJDBCContext() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public boolean isClosed() {
        throw new UnsupportedOperationException("Implement me!");
      }

      @Override
      public LoadQueryInfluencers getLoadQueryInfluencers() {
        throw new UnsupportedOperationException("Implement me!");
      }
    };
    int value;
  }
}
