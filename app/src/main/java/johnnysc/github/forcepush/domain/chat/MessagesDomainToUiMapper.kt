package johnnysc.github.forcepush.domain.chat

import johnnysc.github.forcepush.core.Abstract

/**
 * @author Asatryan on 25.08.2021
 */
interface MessagesDomainToUiMapper<T> : Abstract.Mapper.DomainToUi<List<MessageDomain>, T>